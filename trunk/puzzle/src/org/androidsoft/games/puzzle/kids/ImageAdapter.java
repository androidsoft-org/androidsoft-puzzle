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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Image Adapter
 * @author pierre
 */
public class ImageAdapter extends BaseAdapter
{

    private Context mContext;

    public ImageAdapter(Context c)
    {
        mContext = c;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount()
    {
        return MainActivity.mList.size();
    }

    /**
     * {@inheritDoc}
     */
    public Object getItem(int position)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position)
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(76, 76));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(MainActivity.getResIdAt(position));

        return imageView;
    }
}
