package ca.mcscert.jpipe.model;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalTableTest {
    private GlobalTable globalTable;

    @BeforeEach
    public void setUp() {
        globalTable = GlobalTable.getInstance();
        globalTable.putall(123L, new ImmutablePair<>(null, new ArrayList<>(Collections.singleton(456L))));
        globalTable.putall(456L, new ImmutablePair<>(123L, Arrays.asList(789L, 1011L)));
        globalTable.putall(789L, new ImmutablePair<>(456L, new ArrayList<>()));
        globalTable.putall(1011L, new ImmutablePair<>(789L, new ArrayList<>()));
    }

    @Test
    public void uniqueTable(){
        GlobalTable otherGlobalTable = GlobalTable.getInstance();
        assertEquals(globalTable, otherGlobalTable);
    }


    @Test
    public void childrenFunctions(){
        assertEquals(globalTable.getChilds(456L), Arrays.asList(789L, 1011L));
        // Adding child
        globalTable.putall(1213L, new ImmutablePair<>(789L, new ArrayList<>()));
        // Removing child
        // Getting child

    }

    @Test
    public void parentFunctions(){
        // Adding parent
        // Removing parent
        // Getting parent
    }



}
