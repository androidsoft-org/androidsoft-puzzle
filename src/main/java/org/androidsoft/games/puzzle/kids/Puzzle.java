/* Copyright (c) 2010 Pierre LEVY androidsoft.org
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.androidsoft.games.puzzle.kids;

import android.content.SharedPreferences;
import java.util.ArrayList;

/**
 *
 * @author pierre
 */
public class Puzzle
{

    public interface OnPuzzleListener
    {

        void onComplete(int moveCount);

        void onUpdateView(int moveCount);
    }
    private static final String PREF_LIST = "list";
    private static final String PREF_MOVE_COUNT = "move_count";
    private static final int mWidth = 4;
    private static final int mHeight = 4;
    private Tile mEmptyTile;
    private int mMoveCount;
    static TileList mList = new TileList();
    private OnPuzzleListener mListener;
    private int[] tiles;
    private int[] messages;

    public Puzzle(int[] tiles, int[] messages, OnPuzzleListener listener)
    {
        this.tiles = tiles;
        this.messages = messages;
        mListener = listener;
    }

    void onPosition(int position)
    {
        Tile t = getTileAt(position);

        if (move(t))
        {
            mMoveCount++;
            mListener.onUpdateView(mMoveCount);
            checkComplete();
        }

    }

    int getCount()
    {
        return mList.size();
    }

    int getMaxTilesPerRow()
    {
        return mWidth;
    }

    int getMinTilesPerRow()
    {
        return mWidth;
    }

    int getResId(int position)
    {
        return getTileAt(position).getResId();
    }

    void resume(SharedPreferences prefs)
    {
        String serialized = prefs.getString(PREF_LIST, null);
        if (serialized != null)
        {
            mList = new TileList(serialized);
            mEmptyTile = mList.getTileByResId(R.drawable.empty_tile);
            mMoveCount = prefs.getInt(PREF_MOVE_COUNT, 0);
        }
    }

    void pause(SharedPreferences prefs, boolean quit)
    {
        SharedPreferences.Editor editor = prefs.edit();
        if (!quit)
        {
            // Paused without quit - save state
            editor.putString(PREF_LIST, mList.serialize());
            editor.putInt(PREF_MOVE_COUNT, mMoveCount);
        } else
        {
            editor.remove(PREF_LIST);
            editor.remove(PREF_MOVE_COUNT);
        }
        editor.commit();

    }

    /**
     * Move one or several tiles according the position
     * @param t The tile selected
     * @return True some tiles has been moved, false if no tile has moved
     */
    private boolean move(Tile t)
    {
        Position pos1 = t.getPosition(mWidth);
        Position pos2 = mEmptyTile.getPosition(mWidth);
        if (pos1.mX == pos2.mX)
        {
            int x = pos1.mX;
            int nYmin = pos1.mY;
            int nYmax = pos2.mY;
            if (pos1.mY > pos2.mY)
            {
                nYmin = pos2.mY;
                nYmax = pos1.mY;

                for (int y = nYmin; y < nYmax; y++)
                {
                    Tile t1 = getTile(x, y);
                    Tile t2 = getTile(x, y + 1);
                    t1.swapPosition(t2);
                }
            } else
            {
                for (int y = nYmax; y > nYmin; y--)
                {
                    Tile t1 = getTile(x, y - 1);
                    Tile t2 = getTile(x, y);
                    t1.swapPosition(t2);
                }
            }
            return true;
        }
        if (pos1.mY == pos2.mY)
        {
            int y = pos1.mY;
            int nXmin = pos1.mX;
            int nXmax = pos2.mX;
            if (pos1.mX > pos2.mX)
            {
                nXmin = pos2.mX;
                nXmax = pos1.mX;

                for (int x = nXmin; x < nXmax; x++)
                {
                    Tile t1 = getTile(x, y);
                    Tile t2 = getTile(x + 1, y);
                    t1.swapPosition(t2);
                }
            } else
            {
                for (int x = nXmax; x > nXmin; x--)
                {
                    Tile t1 = getTile(x, y);
                    Tile t2 = getTile(x - 1, y);
                    t1.swapPosition(t2);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Gets a tile from its position into the grid
     * @param nX The X
     * @param nY The Y
     * @return The tile
     */
    private static Tile getTile(int nX, int nY)
    {
        int nPosition = (nY - 1) * mWidth + nX - 1;
        return getTileAt(nPosition);
    }

    private static Tile getTileAt(int position)
    {
        for (Tile t : mList)
        {
            if (t.isAtPosition(position))
            {
                return t;
            }
        }
        return null;
    }

    static int getResIdAt(int nPosition)
    {
        Tile tile = getTileAt(nPosition);
        return tile.getResId();
    }

    /**
     * Check if all pieces has been
     */
    private void checkComplete()
    {
        if (isComplete())
        {
            mListener.onComplete(mMoveCount);
        }
    }

    private boolean isComplete()
    {
        for (Tile t : mList)
        {
            if (!t.isAtTheGoodPosition())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Start a new game
     */
    public void init()
    {
        mMoveCount = 0;
        mList.clear();
        ArrayList<Integer> shuffle = getShuffledList();
        for (int i = 0; i < tiles.length - 1; i++)
        {
            mList.add(new Tile(i, shuffle.get(i), tiles[i]));
        }
        int nLast = tiles.length - 1;
        mEmptyTile = new Tile(nLast, shuffle.get(nLast), R.drawable.empty_tile);
        mList.add(mEmptyTile);

    }

    /**
     * Gets a shuffled list of indexes
     * @return A list of indexes
     */
    private ArrayList<Integer> getShuffledList()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < mWidth * mHeight; i++)
        {
            double dPos = Math.random() * list.size();
            int nPos = (int) dPos;
            list.add(nPos, new Integer(i));
        }
        return list;
    }
}
