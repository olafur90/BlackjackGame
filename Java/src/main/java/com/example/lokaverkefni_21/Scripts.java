package com.example.lokaverkefni_21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Scripts {
    public String RunScript(String url) {
        String[] cmd = new String[]{"sh", url};
        String output;
        try {
            Process pr = Runtime.getRuntime().exec(cmd);
            pr.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            output = reader.readLine();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            output = e.getMessage();
        }
        return output;
    }
}
