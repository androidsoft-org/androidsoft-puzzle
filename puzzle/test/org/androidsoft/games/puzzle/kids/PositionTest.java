/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.androidsoft.games.puzzle.kids;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pierre
 */
public class PositionTest {

    public PositionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod()
    {
        Position pos = new Position( 5 , 4 );
        assertTrue( pos.mX == 2 );
        assertTrue( pos.mY == 2 );

        pos = new Position( 10 , 4 );
        assertTrue( pos.mX == 3 );
        assertTrue( pos.mY == 3 );

        pos = new Position( 0 , 4 );
        assertTrue( pos.mX == 1 );
        assertTrue( pos.mY == 1 );

        pos = new Position( 3 , 4 );
        assertTrue( pos.mX == 4 );
        assertTrue( pos.mY == 1 );
    }



}