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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * MainActivity
 * @author pierre
 */
public class MainActivity extends AbstractMainActivity
{

    private static final String PREF_LIST = "list";
    private static final String PREF_MOVE_COUNT = "move_count";
    private static final int TOAST_FREQUENCY = 10;
    private static final int mWidth = 4;
    private static final int mHeight = 4;
    private final static int[] tiles =
    {
        R.drawable.item_1, R.drawable.item_2,
        R.drawable.item_3, R.drawable.item_4, R.drawable.item_5, R.drawable.item_6,
        R.drawable.item_7, R.drawable.item_8, R.drawable.item_9, R.drawable.item_10,
        R.drawable.item_11, R.drawable.item_12, R.drawable.item_13, R.drawable.item_14,
        R.drawable.item_15, R.drawable.item_16
    };
    private final static int[] messages =
    {
        R.string.message_1, R.string.message_2,
        R.string.message_3, R.string.message_4, R.string.message_5,
        R.string.message_6, R.string.message_7, R.string.message_8,
        R.string.message_9, R.string.message_10, R.string.message_11
    };
    private Tile mEmptyTile;
    private int mMoveCount;
    private GridView mGridView;
    static TileList mList = new TileList();

    /**
     * {@inheritDoc }
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        ImageView image = (ImageView) findViewById(R.id.second_logo);
        image.setImageResource(R.drawable.second_logo);

        newGame();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected View getGameView()
    {
        return mGridView;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void newGame()
    {
        initData();
        if (mGridView == null)
        {
            initGrid();
        }
        drawGrid();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        SharedPreferences prefs = getPreferences(0);

        String serialized = prefs.getString(PREF_LIST, null);
        if (serialized != null)
        {
            mList = new TileList(serialized);
            mEmptyTile = mList.getTileByResId(R.drawable.empty_tile);
            mMoveCount = prefs.getInt(PREF_MOVE_COUNT, 0);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences.Editor editor = getPreferences(0).edit();
        if( !mQuit )
        {
            // Paused without quit - save state
            editor.putString(PREF_LIST, mList.serialize());
            editor.putInt(PREF_MOVE_COUNT, mMoveCount);
        }
        else
        {
            editor.remove(PREF_LIST );
            editor.remove(PREF_MOVE_COUNT );
        }
        editor.commit();
    }

    /**
     * Initialize the grid
     */
    private void initGrid()
    {
        mGridView = (GridView) findViewById(R.id.gridview);

        mGridView.setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Tile t = getTileAt(position);

                if (move(t))
                {
                    mMoveCount++;
                    drawGrid();
                    toast(mMoveCount);
                    checkComplete();
                }

            }
        });

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
     * Draw or redraw the grid
     */
    private void drawGrid()
    {
        mGridView.setAdapter(new ImageAdapter(MainActivity.this));

    }

    /**
     * Check if all pieces has been
     */
    private void checkComplete()
    {
        if (isComplete())
        {
            showEndDialog();
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
    private void initData()
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

    private void toast(int nMoveCount)
    {
        if ((nMoveCount > 0) && (nMoveCount % TOAST_FREQUENCY == 0))
        {
            double dPos = Math.random() * messages.length;
            int nPos = (int) dPos;
            String message = this.getString(messages[nPos]);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }



}
