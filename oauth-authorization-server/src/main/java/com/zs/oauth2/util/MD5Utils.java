package com.zs.oauth2.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Slf4j
public class MD5Utils {

    /**
     * 执行密码加密
     *
     * @param password 原始密码
     * @param salt     盐值
     * @return 加密后的密文
     */
    public static String getMd5Password(String password, String salt) {

        /*
         * 加密规则：
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次
         */
        for (int i = 0; i < 4; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }

    /**
     * 密码核对
     *
     * @param dbSalt        数据库盐值
     * @param password      前端传参密码
     * @param dbMd5Password 数据库加密后的密码
     * @return true-密码相等 false-密码不相等
     */
    public static Boolean comparePasswordsForEquality(String dbSalt, String dbMd5Password, String password) {
        String md5Password = MD5Utils.getMd5Password(password, dbSalt);
        return dbMd5Password.equals(md5Password);
    }

    public static void main(String[] args) {

        //1.生成md5+盐值密码
        String salt = UUID.randomUUID().toString().toUpperCase();
        //原始密码
        String password = "123456";
        //数据库加密后的密码
        String dbMd5Password = MD5Utils.getMd5Password(password, salt);
        log.info("md5加密+盐值后的密码->{}", dbMd5Password);

        //前端传参密码
        String frontPagePasswordParam = "123456";
        //数据库盐值
        String dbSalt = salt;

        if (MD5Utils.comparePasswordsForEquality(dbSalt, dbMd5Password, frontPagePasswordParam)) {
            System.out.println("密码相同");
        } else {
            System.out.println("密码不相同");
        }

    }
}
