package org.example;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.out;

@Slf4j
public class RedisStorage {
    private RedissonClient redisson;

    private RKeys rKeys;

    private RScoredSortedSet<String> users;

    private final static String KEY = "USERS";

    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        users = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    public void addUser(int id) {
        users.add(id, String.valueOf(id));
    }

    public void addUser(int id, double value) {
        users.add(value, String.valueOf(id));
    }

    public void shutdown() {
        redisson.shutdown();
    }

    public void print() {
        for (double i = 1; i < 21; i += 0.25) {
            users.valueRange(i, true, i, true).forEach(el -> {
                System.out.println("— На главной странице показываем пользователя " + el);
                try {
                    randomUpdate(Integer.parseInt(el) + 1);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    private void randomUpdate(int id) throws InterruptedException {

        double probability = 0.10;
        boolean result = getTrueWithProbability(probability);

        if (result) {
            update(id);
        }
    }

    private void update(int id) {

        if (id < 20) {
            int userId = new Random().nextInt(id, 21);

            users.remove(String.valueOf(userId));
            addUser(userId, id - 0.5);

            out.println("> Пользователь " + userId + " оплатил платную услугу");
        }
    }

    public static boolean getTrueWithProbability(double probability) {
        return new Random().nextDouble() < probability;
    }

}
