package com.maxcloud.renter.util.file;

import com.maxcloud.renter.util.L;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by MAX-XXY on 2016/2/18.
 */
public class TextFileReader {
    private FileInputStream fis = null;
    private BufferedReader br = null;
    private InputStreamReader isr = null;

    public TextFileReader(File file) throws FileNotFoundException {
        fis = new FileInputStream(file);
        isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);
    }

    public String readLine() throws IOException {
        if (null == br) {
            return null;
        }

        if (br.ready()) {
            return br.readLine();
        } else {
            return null;
        }
    }

    public void close() {
        try {
            if (null != br) {
                br.close();
            }
        } catch (Exception e) {
            L.e("close", e);
        }
        try {
            if (null != isr) {
                isr.close();
            }
        } catch (Exception e) {
            L.e("close", e);
        }
        try {
            if (null != fis) {
                fis.close();
            }
        } catch (Exception e) {
            L.e("close", e);
        }
    }
}
