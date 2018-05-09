package tokenManagement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TokenManagement {
	
	//***********************
	// Singletonデザイン
	//***********************
    private TokenManagement(){}

    public static TokenManagement getInstace() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final TokenManagement INSTANCE = new TokenManagement();
    }
    
    // 有効時間 10min
    private static final long TIME_OUT_MS = 10 * 60 * 1000;
    
    // clear実行容量
    private static final long MAX_COUNT = 1000;
     
    // 共有tokens
    private static HashMap<String, Long> tokensBank = new HashMap<String, Long>();
    
    // 生成と保存
    public String generateAndStore(){
    	
    	// 生成する毎、容量を判定、指定時間範囲外のtokenを削除
    	if(tokensBank.size() > MAX_COUNT){
    		removeTimeout();
    	}
    	
    	String newTokenId = UUID.randomUUID().toString();
    	tokensBank.put(newTokenId, System.currentTimeMillis());
    	return newTokenId;
    }
    
    // チェック
    public Boolean check(String tokenId){
        // 存在、かつ、指定範囲内なら、アクセス時間を更新
    	if(tokensBank.containsKey(tokenId)
    			&& (System.currentTimeMillis() - tokensBank.get(tokenId) <= TIME_OUT_MS)){
    		tokensBank.put(tokenId, System.currentTimeMillis());
    		return true;
    	} else {
    		return false;
    	}
    }
    
    // clear
    public void clear(){
    	tokensBank.clear();
    }
    
    private void removeTimeout(){
    	
    	List<String> deleteTokens = new LinkedList<String>();
    	
    	// 指定範囲外を洗い出す
    	for(HashMap.Entry<String, Long> entry : tokensBank.entrySet()) {
    		if(System.currentTimeMillis() - entry.getValue() >= TIME_OUT_MS){
    			deleteTokens.add(entry.getKey());
    		}
    	}
    	
    	//　削除
        for(String token : deleteTokens){
        	tokensBank.remove(token);
        }
    }
}
