package com.firebase.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FirebaseCredentialStore implements CredentialStore {

    private final Map<String, String> credentials;

    private FirebaseCredentialStore() {
        credentials = new ConcurrentHashMap<>();
    }

    @Override
    public String loadCredential(String firebaseId, String sessionId) {
        return credentials.get(firebaseId + "/" + sessionId);
    }

    @Override
    public boolean storeCredential(String firebaseId, String sessionId, String credential) {
        credentials.put(firebaseId + "/" + sessionId, credential);
        return true;
    }

    @Override
    public boolean clearCredential(String firebaseId, String sessionId) {
        return credentials.remove(firebaseId + "/" + sessionId) != null;
    }

    public static void init() {
        Config config = Firebase.getDefaultConfig();
        config.setCredentialStore(new FirebaseCredentialStore());
        Firebase.setDefaultConfig(config);
    }
}
