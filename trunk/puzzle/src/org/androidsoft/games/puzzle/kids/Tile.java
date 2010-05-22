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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tile
 * @author pierre@androidsoft.org
 */
public class Tile
{
    private static final String GOOD_POSITION = "GoodPosition";
    private static final String CURRENT_POSITION = "CurrentPosition";
    private static final String RES_ID = "ResId";

    private int mGoodPosition;
    private int mCurrentPosition;
    private int mResId;

    Tile(int nGood, int nCurrent, int nResId)
    {
        mGoodPosition = nGood;
        mCurrentPosition = nCurrent;
        mResId = nResId;

    }

    Tile(JSONObject object)
    {
        try
        {
            mGoodPosition = object.getInt(GOOD_POSITION);
            mCurrentPosition = object.getInt(CURRENT_POSITION);
            mResId = object.getInt(RES_ID);
        }
        catch (JSONException ex)
        {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    Position getPosition(int nWidth)
    {
        return new Position(mCurrentPosition, nWidth);
    }

    int getResId()
    {
        return mResId;
    }

    void swapPosition(Tile t)
    {
        int nTilePosition = t.mCurrentPosition;
        t.mCurrentPosition = this.mCurrentPosition;
        this.mCurrentPosition = nTilePosition;
    }

    boolean isAtTheGoodPosition()
    {
        return mCurrentPosition == mGoodPosition;
    }

    boolean isAtPosition(int position)
    {
        return mCurrentPosition == position;
    }

    JSONObject json()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate( GOOD_POSITION, mGoodPosition );
            object.accumulate( CURRENT_POSITION, mCurrentPosition );
            object.accumulate( RES_ID, mResId );
        }
        catch (JSONException ex)
        {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return object;

    }
}
