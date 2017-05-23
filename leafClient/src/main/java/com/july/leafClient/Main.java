package com.july.leafClient;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by haoyifen on 2017/5/23 12:34.
 */
public class Main {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        Set<Long> total = IntStream.range(0, 20)
                .parallel()
                .mapToObj(it -> {
                    LeafSequencer user = new LeafSequencer("user");
                    Set<Long> longs = IntStream.range(0, 20000).mapToObj(i -> user.nextSeq()).collect(Collectors.toSet());
                    return longs;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        System.out.println(total.size());
        System.out.println(System.currentTimeMillis()-l);
    }
}
