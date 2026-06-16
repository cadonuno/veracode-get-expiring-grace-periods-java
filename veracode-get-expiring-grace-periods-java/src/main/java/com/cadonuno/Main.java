package com.cadonuno;

import com.cadonuno.api.Credentials;

public class Main {

    public static void main(String[] args) {
        Credentials.VERACODE_API_ID = args[0];
        Credentials.VERACODE_API_SECRET = args[1];

        GetExpiringGracePeriods.run();
    }
}