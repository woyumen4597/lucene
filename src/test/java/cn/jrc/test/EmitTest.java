package cn.jrc.test;

import cn.jrc.spider.rmdup.trie.Emit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class EmitTest {

    @Test
    public void equals() {
        Emit one = new Emit(13, 42, null);
        Emit two = new Emit(13, 42, null);
        assertEquals(one, two);
    }

    @Test
    public void notEquals() {
        Emit one = new Emit(13, 42, null);
        Emit two = new Emit(13, 43, null);
        assertNotSame(one, two);
    }

}
