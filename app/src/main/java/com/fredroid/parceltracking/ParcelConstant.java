package com.fredroid.parceltracking;

import android.arch.persistence.room.Ignore;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by jackttc on 29/12/17.
 */

public class ParcelConstant {
    public static final int COMMPANY_ewe = 0;
    public static final int COMMPANY_freaky_quick = 1;
    public static final int COMMPANY_fast_go = 2;
    public static final int COMMPANY_star_ex = 3;
    public static final int COMMPANY_speed = 4;
    public static final int COMMPANY_changjiang = 5;
    public static final int COMMPANY_other = 4;


    public static final int MAX_PHOTO = 4;

  //  public static final String [] glbCommpanyNameList = {"EWE","Freak Quick",""};

    public static int getCommpanyId(String barcode) {

     //   String barcode = item.sParcel2DBar;
     //   String sCode = item.getsParcel2DBar();
        int nCommpany = COMMPANY_other;
        if (barcode.matches("FQ\\d\\d\\d\\d\\d\\d")) {
            nCommpany = ParcelConstant.COMMPANY_freaky_quick;
        }
        if (barcode.matches("FG\\d{7}AU")) {
            nCommpany = ParcelConstant.COMMPANY_fast_go;
        }
        if (barcode.matches("SE\\d{7}AU")) {
            nCommpany = ParcelConstant.COMMPANY_star_ex;
        }
        if (barcode.matches("B\\d{9}B") || barcode.matches("PFIR\\d{7}")) {
            nCommpany = ParcelConstant.COMMPANY_ewe;
        }
        if (barcode.matches("speed")) {
            nCommpany = ParcelConstant.COMMPANY_speed;
        }
        if (barcode.matches("CE\\d{9}AU")) {
            nCommpany = ParcelConstant.COMMPANY_changjiang;
        }
        return nCommpany;
    }
}
