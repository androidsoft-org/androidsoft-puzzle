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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * MainActivity
 * @author pierre
 */
public class MainActivity extends AbstractMainActivity implements Puzzle.OnPuzzleListener
{

    private static final int TOAST_FREQUENCY = 10;
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
    private Puzzle mPuzzle = new Puzzle(tiles, messages, this);
    private PuzzleView mPuzzleView;

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
        return findViewById(R.id.gameview);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void newGame()
    {
        mPuzzle.init();
        if (mPuzzleView == null)
        {
            mPuzzleView = (PuzzleView) findViewById(R.id.gridview);
            mPuzzleView.setPuzzle(mPuzzle);
//            int width = getGameView().getWidth();
//            int height = getGameView().getHeight();
//            int size = ( width < height ) ? width : height;
//            mPuzzleView.setColumnWidth( size / 4 );
        }
        drawGrid();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void about()
    {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPuzzle.resume(prefs);

    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPuzzle.pause( prefs, mQuit);
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

    public void onComplete(int moveCount)
    {
        showEndDialog();
    }

    public void onUpdateView(int moveCount)
    {
        drawGrid();
        toast(moveCount);
    }

    private void drawGrid()
    {
        mPuzzleView.update();
    }
}
