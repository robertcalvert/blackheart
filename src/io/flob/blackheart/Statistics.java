/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      Statistics.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import java.security.MessageDigest;

/**
 *
 * @author rob
 */
public final class Statistics {

    private int points;
    private int mobs_killed;
    private int shots_fired;
    private int secrets_found;
    private int prisoners_killed;
    private int prisoners_rescued;

    public int points() {
        return points;
    }

    public void points(int amount) {
        points += amount;
    }
    
    public int mobs_killed() {
        return mobs_killed;
    }
    
    public void mobs_killed(int amount) {
        mobs_killed += amount;
    }
    
     public int prisoners_killed() {
        return prisoners_killed;
    }
    
    public void prisoners_killed(int amount) {
        prisoners_killed += amount;
    }
    
     public int prisoners_rescued() {
        return prisoners_rescued;
    }
    
    public void prisoners_rescued(int amount) {
        prisoners_rescued += amount;
    }
    
    public int shots_fired() {
        return shots_fired;
    }
    
    public void shots_fired(int amount) {
        shots_fired += amount;
    }
    
    public int secrets_found() {
        return secrets_found;
    }
    
    public void secrets_found(int amount) {
        secrets_found += amount;
    }

    public void merge(Statistics _statistics) {
        points(_statistics.points());
        mobs_killed(_statistics.mobs_killed());
        shots_fired(_statistics.shots_fired());
        secrets_found(_statistics.secrets_found());
        prisoners_killed(_statistics.prisoners_killed());
        prisoners_rescued(_statistics.prisoners_rescued());
    }
    
    public String hash() throws Exception {
        String input = About.title;
        input = input + points;
        input = input + shots_fired;
        input = input + mobs_killed;
        input = input + prisoners_killed;
        input = input + prisoners_rescued;
        input = input + secrets_found;
        return sha1(input);
    }
    
    private String sha1(String value) throws Exception {
        byte[] bytes_of_value = value.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return bytearray_to_hex_string(md.digest(bytes_of_value));
    }

    private String bytearray_to_hex_string(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result +=
                    Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}