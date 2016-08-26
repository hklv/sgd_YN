package com.ztesoft.uboss.bpm.utils;


import com.ztesoft.zsmart.pot.common.ValidateCode;

import java.util.Random;

/**
 * 继承自ValidateCode
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/5/13
 */
public class MyValidateCode extends ValidateCode {
    private char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public String getRandomCode(int codeCount) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < codeCount; i++) {
            Random random = new Random();
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            sb.append(strRand);
        }
        return sb.toString();
    }
}
