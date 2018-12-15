package com.fredroid.parceltracking;

import android.arch.persistence.room.Ignore;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by jackttc on 29/12/17.
 */

public class ParcelConstant {

    public static final int COMMPANY_ewe = 0;
    public static final int COMMPANY_fast_go = 1;
    public static final int COMMPANY_star_ex = 2;
    public static final int COMMPANY_transrush = 3;
    public static final int COMMPANY_other = 4;

    public static final int MAX_PHOTO = 4;

    public static int getCommpanyId(String barcode) {
        int nCommpany = COMMPANY_other;
        if (barcode.matches("FG\\d{7}AU")) {
            nCommpany = ParcelConstant.COMMPANY_fast_go;
        }
        else if (barcode.matches("SE\\d{7}AU")) {
            nCommpany = ParcelConstant.COMMPANY_star_ex;
        }
        else if (barcode.matches("[a-zA-Z]\\d{9}[a-zA-Z]") || barcode.matches("PFIR\\d{7}")) {
            nCommpany = ParcelConstant.COMMPANY_ewe;
        }
        else if (barcode.matches("[a-zA-Z][a-zA-Z]\\d{11}")) {
            nCommpany = ParcelConstant.COMMPANY_transrush;
        }
        return nCommpany;
    }
}
