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

/**
 * Position
 * @author pierre
 */
public class Position
{
    int mX;
    int mY;


    Position( int nPosition , int nWidth )
    {

        mX = 1 + ( nPosition  % nWidth );
        mY = 1 + ( nPosition / nWidth );
    }

    /*
    boolean isContigue( Position p )
    {
        if( ((mX == p.mX) && (mY - p.mY == 1)) ||
                ((mX == p.mX) && (mY - p.mY == -1)) ||
                ((mY == p.mY) && (mX - p.mX == 1)) ||
                ((mY == p.mY) && (mX - p.mX == -1)))
        {
            return true;
        }
        return false;
    }
*/

}
