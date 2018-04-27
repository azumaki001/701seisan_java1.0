package com.server.rest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.server.entity.Detail;
import com.server.entity.HelloResult;
import com.server.entity.UserAUserB_RESULT;
import com.server.entity.UserList;
import com.server.entity.UserMst;
import com.server.retpojo.EventList;
import com.server.retpojo.PayList;
import com.server.retpojo.UserAUserBResultList;
import com.server.retpojo.UserMstAndPayList;
import com.server.revpojo.AdjustParam;
import com.server.revpojo.EventDetail;
import com.server.revpojo.PayBizMst;
import com.server.revpojo.UserAUserBSelKey;

@Path("/SeisanRestApi/")
public class SeisanRestful {

    public SeisanRestful() {
        initConn ();
    }

    private static Connection conn = null;
    private static SqlSessionFactory factory = null;

    private static List <UserMst> userList = null;
    private static HashMap<String, String> userMap = null;

    private static SqlSessionFactory getFactory () {
        if (factory != null) {
            return factory;
        }
        InputStream in = SeisanRestful.class.getResourceAsStream("../../../resources/mybatis-config.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
        return factory;
    }

    private static void initConn() {

//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        InputStream inss = classLoader.getResourceAsStream("resources/mybatis-config.xml");
        SqlSession session = null;
        try {
            factory = getFactory();
            userMap = new HashMap<String, String> ();
            session = factory.openSession();
            userList = session.selectList("seisan.rest.oracle.SELECT_FROM_USERMST");
            Iterator ulI = userList.iterator();
            while (ulI.hasNext()) {
                UserMst tmepUm = (UserMst) ulI.next();
                userMap.put(tmepUm.getId().toString(), tmepUm.getName());
            }
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/api/v1")
    @Produces(MediaType.APPLICATION_JSON)
    public String initMain() {

        List<PayList> allList = null;
        List<PayList> plList = new ArrayList<PayList> ();
        List<PayList> miPlList = new ArrayList<PayList> ();
        SqlSession session = null;
        try {
            factory = getFactory();
            session = factory.openSession();
            allList = session.selectList("seisan.rest.oracle.SELECT_SIHARAI");

            for (int i=0;i<allList.size();i++) {
                ArrayList<Detail> tempDl = allList.get(i).getDetails();
                boolean doneFlg = true;
                boolean yetFlg = true;

                Iterator dIt = tempDl.iterator();
                while (dIt.hasNext()) {
                    Detail tempD = (Detail) dIt.next();
                    if ("0".equals(tempD.getDone())) {
                        doneFlg = false;
                    }
                    if ("1".equals(tempD.getDone())) {
                        yetFlg = false;
                    }
                }

                String payer = allList.get(i).getPayer();
                allList.get(i).setPayer(userMap.get(payer));

                String[] payForusers = allList.get(i).getPayForusers().split(",");
                StringBuffer sb = new StringBuffer ();
                for (int j=0;j<payForusers.length;j++) {
                    if (j != 0) {
                        sb.append(",");
                    }
                    sb.append(userMap.get(payForusers[j]));
                }
                allList.get(i).setPayForusers(sb.toString());

                if (!doneFlg && !yetFlg) {
                	allList.get(i).setAdDone("2");
                	miPlList.add(allList.get(i));
                } else if (doneFlg && !yetFlg) {
                	allList.get(i).setAdDone("1");
                	plList.add(allList.get(i));
                } else if (!doneFlg && yetFlg) {
                	allList.get(i).setAdDone("0");
                	miPlList.add(allList.get(i));
                }
            }
        } finally {
            session.close();
        }

        UserMstAndPayList umapl = new UserMstAndPayList ();
        umapl.setUserMst(userList);
        umapl.setPayList(plList);
        umapl.setMiPayList(miPlList);

        JSONObject object = JSONObject.fromObject(umapl);
        return object.toString();
    }

    @POST
    @Path("/api/v2")
    @Produces(MediaType.TEXT_PLAIN)
    public String createPayBiz(String str) {

        JSONObject object = JSONObject.fromObject(str);
        PayBizMst bean = (PayBizMst)JSONObject.toBean(object, PayBizMst.class);

        SqlSession session = null;
        BigDecimal payId = null;
        try {
            factory = getFactory();
            session = factory.openSession();

            // payBizテーブルへの登録
            payId = session.selectOne("seisan.rest.oracle.SELECT_PAYID");
            Map<String, Object> paramMap = new HashMap <String, Object> ();
            paramMap.put("PAY_ID", payId);
            paramMap.put("PAY_PAYER", bean.getPayer());
            paramMap.put("PAY_FOR_USERS", bean.getPayForusers());
            paramMap.put("PAY_CONTENT", bean.getPayContent());
            paramMap.put("PAY_AMOUNT", bean.getPayAmount());
            session.insert("seisan.rest.oracle.INSERT_PAY_BIZ", paramMap);

            // detialsテーブルへの登録
            String[] users = bean.getPayForusers().split(",");
            BigDecimal amount = bean.getPayAmount();
            BigDecimal adAdmout = amount.divide(new BigDecimal(users.length), 0, BigDecimal.ROUND_HALF_UP);
            for (int i=0;i<users.length;i++) {
                if (bean.getPayer().equals(users[i])) {
                    continue;
                }
                BigDecimal adDetailId = session.selectOne("seisan.rest.oracle.SELECT_ADDETAILID");
                paramMap = new HashMap <String, Object> ();
                paramMap.put("AD_DETAIL_ID", adDetailId);
                paramMap.put("PAY_ID", payId);
                paramMap.put("AD_AMOUNT", adAdmout);
                paramMap.put("AD_FROM", new BigDecimal(users[i]));
                paramMap.put("AD_TO", new BigDecimal(bean.getPayer()));
                paramMap.put("AD_DONE", "0");
                session.insert("seisan.rest.oracle.INSERT_AD_DETAIL", paramMap);
            }
            session.commit();
        }catch (Exception e) {
            return "{\"result\":\"NG\"}";
        } finally {
            session.close();
        }
        return "{\"result\":\"OK\"}";
    }

    @POST
    @Path("/api/v3")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDetailsByUsers(String str) {
        JSONObject object = JSONObject.fromObject(str);
        UserAUserBSelKey bean = (UserAUserBSelKey)JSONObject.toBean(object, UserAUserBSelKey.class);

        SqlSession session = null;
        UserAUserBResultList detailListAB = new UserAUserBResultList ();
        try {
            factory = getFactory();
            session = factory.openSession();

            // AtoBの結果を取得
            Map<String,Object> paramMap = new HashMap<String,Object> ();
            paramMap.put("userA", bean.getUserA());
            paramMap.put("userB", bean.getUserB());
            List<UserAUserB_RESULT> userAtoUserBList = session.selectList("seisan.rest.oracle.SELECT_ATOB", paramMap);
            detailListAB.setDetailListAB(userAtoUserBList);

            // BtoAの結果を取得
            paramMap = new HashMap<String,Object> ();
            paramMap.put("userA", bean.getUserB());
            paramMap.put("userB", bean.getUserA());
            List<UserAUserB_RESULT> userBtoUserAList = session.selectList("seisan.rest.oracle.SELECT_ATOB", paramMap);
            detailListAB.setDetailListBA(userBtoUserAList);
        } finally {
            session.close();
        }

        JSONObject result = JSONObject.fromObject(detailListAB);
        return result.toString();
    }

    @POST
    @Path("/api/v4")
    @Produces(MediaType.TEXT_PLAIN)
    public String doAdjust(String str) throws SQLException {
        JSONObject object = JSONObject.fromObject(str);
        AdjustParam bean = (AdjustParam)JSONObject.toBean( object, AdjustParam.class );

        SqlSession session = null;

        try {
            factory = getFactory();
            session = factory.openSession();

            // AD_EVENT_IDをシークエンスから取得
            BigDecimal adEventId = session.selectOne("seisan.rest.oracle.SELECT_ADEVENTID");

            // AD_EVENT挿入開始
            Map<String,Object> paramMap = new HashMap<String,Object> ();
            paramMap.put("EVENT_ID", adEventId);
            paramMap.put("DETAIL_IDS", bean.getAdIds());
            paramMap.put("EVENT_AMOUNT", bean.getAdAmount());
            paramMap.put("EVENT_FROM", bean.getUserIdFrom());
            paramMap.put("EVENT_TO", bean.getUserIdTo());
            session.insert("seisan.rest.oracle.INSERT_AD_EVENT", paramMap);

            // AD_DETAILS更新
            String[] adIdsList = bean.getAdIds().split(",");
            session.update("seisan.rest.oracle.UPDATE_ADJUSTMENT_DETAIL", adIdsList);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            return "{\"result\":\"NG\"}";
        } finally {
            session.close();
        }
        return "{\"result\":\"OK\"}";
    }

    @POST
    @Path("/api/v5")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEvent(String str) {

        SqlSession session = null;
        EventList eventList = new EventList ();
        List<EventDetail> edl = new ArrayList<EventDetail> ();

        try {
            factory = getFactory();
            session = factory.openSession();

            edl = session.selectList("seisan.rest.oracle.SELECT_EVENT");
            eventList.setAdEvents(edl);
        } catch (Exception e) {
            return "{\"result\":\"NG\"}";
        } finally {
            session.close();
        }
        JSONObject result = JSONObject.fromObject(eventList);
        return result.toString();
    }

    @POST
    @Path("/api/v6")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDetailsByIds(String str) {

        JSONObject object = JSONObject.fromObject(str);
        EventDetail bean = (EventDetail)JSONObject.toBean( object, EventDetail.class );

        SqlSession session = null;
        EventDetail ed = new EventDetail ();
        UserAUserBResultList resultList= new UserAUserBResultList ();
        List<UserAUserB_RESULT> detailListAB = new ArrayList<UserAUserB_RESULT>();
        List<UserAUserB_RESULT> detailListBA = new ArrayList<UserAUserB_RESULT>();

        try {
            factory = getFactory();
            session = factory.openSession();
            ed = session.selectOne("seisan.rest.oracle.SELECT_EVENT_BY_ID", bean.getEventId());

            String[] detailIds = ed.getEventDetail().split(",");
            Map<String,Object> paramMap = new HashMap<String,Object> ();
            paramMap.put("userA", ed.getEventFrom());
            paramMap.put("userB", ed.getEventTo());
            paramMap.put("detailIds", detailIds);
            detailListAB = session.selectList("seisan.rest.oracle.SELECT_ATOB_WITH_DETAILID",paramMap);
            resultList.setDetailListAB(detailListAB);

            paramMap = new HashMap<String,Object> ();
            paramMap.put("userA", ed.getEventTo());
            paramMap.put("userB", ed.getEventFrom());
            paramMap.put("detailIds", detailIds);
            detailListBA = session.selectList("seisan.rest.oracle.SELECT_ATOB_WITH_DETAILID",paramMap);
            resultList.setDetailListBA(detailListBA);
        } catch (Exception e) {
            return "{\"result\":\"NG\"}";
        } finally {
            session.close();
        }

        JSONObject result = JSONObject.fromObject(resultList);
        return result.toString();
    }

    @GET
    @Path("/api/v0")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserMst() {
        UserList ul = new UserList ();
        ul.setUserList(userList);
        JSONObject object = JSONObject.fromObject(ul);
        return object.toString();
    }

    @POST
    @Path("/api/v10")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HelloResult testJson(@Context HttpHeaders headers) {

        List<String> h = headers.getRequestHeader("x-ces-jwt");
        MultivaluedMap<String, String> map = headers.getRequestHeaders();

        List<String> a = map.get("x-ces-jwt");

        HelloResult result = new HelloResult();
        result.mebr_no="99900213";
        result.cust_id="7765234";
        result.ufpr_mebr_no="001";
        result.able_i_nam="able_i_nam000";
        result.flg="1";
        return result;
    }
}
