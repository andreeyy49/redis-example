package org.example;

public class Main {

    public static void main(String[] args) {
        RedisStorage redis = new RedisStorage();
        redis.init();

        while (true) {
            for (int i = 1; i <= 20; i++) {
                redis.addUser(i);
            }

            redis.print();

        }
    }
}