package cn.jrc.test;

import cn.jrc.spider.rmdup.trie.State;
import cn.jrc.spider.rmdup.trie.UnicodeState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class StateTest {

    @Test
    public void constructSequenceOfCharacters() {
        State rootState = new UnicodeState();
        rootState
            .addState('a')
            .addState('b')
            .addState('c');
        State currentState = rootState.nextState('a');
        assertEquals(1, currentState.getDepth());
        currentState = currentState.nextState('b');
        assertEquals(2, currentState.getDepth());
        currentState = currentState.nextState('c');
        assertEquals(3, currentState.getDepth());
    }

}
