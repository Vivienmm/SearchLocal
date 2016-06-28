package com.su.searchlocaldemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.su.searchlocaldemo.Entity.AppInfo;
import com.su.searchlocaldemo.Entity.Contact;
import com.su.searchlocaldemo.Entity.SearchUtil;

import java.util.ArrayList;

/**
 * Created by chinaso on 2016/6/27.
 * 包含两种实体
 */
public class LocalObjectAdapter extends BaseAdapter {
    private ArrayList<SearchUtil> list = new ArrayList();
    private Context mContext;
    private LayoutInflater mInflater;

    private static final int LAYOUT_APP = 0;// 3种不同的布局
    private static final int LAYOUT_CONTRACT = 1;
    private static final int LAYOUT_BOARD = 2;


    public LocalObjectAdapter(Context context, ArrayList arryList) {
        list = arryList;
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        SearchUtil mEntity = list.get(position);
        int type = 0;
        switch (mEntity.setEntity()) {
            case 1:
                type = LAYOUT_APP;
                break;
            case 2:
                type = LAYOUT_CONTRACT;
                break;
            case 3:
                type = LAYOUT_BOARD;
                break;

        }
        return type;
    }

    class ViewHolderAPP {
        public TextView appName;
        public TextView pkgName;
        public ImageView appIcon;
    }

    class ViewHolderContract {

        public TextView tvTitle;
        public TextView tvNumber;
        public ImageButton dialbtn;
        public ImageButton mesbtn;
    }

    class ViewHolderBoard {
        public Button moreBtn;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        int type = getItemViewType(i);
        ViewHolderAPP holderAPP = null;
        ViewHolderContract holderContract = null;
        ViewHolderBoard holderBoard = null;
        if (view == null) {
            switch (type) {
                case LAYOUT_APP:
                    holderAPP = new ViewHolderAPP();
                    view = mInflater.inflate(R.layout.app_item, null);
                    holderAPP.appIcon = (ImageView) view
                            .findViewById(R.id.app_icon);
                    holderAPP.appName = (TextView) view
                            .findViewById(R.id.app_name);
                    holderAPP.pkgName = (TextView) view
                            .findViewById(R.id.pkg_name);

                    view.setTag(holderAPP);
                    break;
                case LAYOUT_CONTRACT:
                    holderContract = new ViewHolderContract();
                    view = mInflater.inflate(R.layout.item_contact, null);
                    holderContract.tvTitle = (TextView) view.findViewById(R.id.title);
                    holderContract.tvNumber = (TextView) view.findViewById(R.id.number);
                    holderContract.dialbtn = (ImageButton) view.findViewById(R.id.dial);
                    holderContract.mesbtn = (ImageButton) view.findViewById(R.id.message);

                    view.setTag(holderContract);

                    break;
                case LAYOUT_BOARD:
                    holderBoard = new ViewHolderBoard();
                    view = mInflater.inflate(R.layout.item_board, null);
                    holderBoard.moreBtn = (Button) view.findViewById(R.id.morebtn);
                    view.setTag(holderBoard);
                    break;
            }
        } else {
            switch (type) {
                case LAYOUT_APP:
                    holderAPP = (ViewHolderAPP) view.getTag();
                    break;
                case LAYOUT_CONTRACT:
                    holderContract = (ViewHolderContract) view.getTag();
                    break;
                case LAYOUT_BOARD:
                    holderBoard = (ViewHolderBoard) view.getTag();
                    break;
            }

        }
        switch (type) {
            case LAYOUT_APP:
                final AppInfo appInfo = (AppInfo) getItem(i);
                holderAPP.appIcon.setImageDrawable(appInfo.appIcon);
                holderAPP.appName.setText(appInfo.appName);
                holderAPP.pkgName.setText(appInfo.pkgName);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.startActivity(appInfo.appIntent);
                    }
                });
                break;
            case LAYOUT_CONTRACT:
                final Contact contactInfo = (Contact) getItem(i);
                holderContract.tvTitle.setText(contactInfo.name);
                holderContract.tvNumber.setText(contactInfo.number);

                holderContract.dialbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tel = "tel:" + contactInfo.number;
                        //Uri uri = Uri.parse("tel:xxxxxx");
                        Uri uri = Uri.parse(tel);
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        mContext.startActivity(intent);
                    }
                });
                holderContract.mesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = "smsto:" + contactInfo.number;
                        //Uri uri = Uri.parse("smsto:"+要发送短信的对方的number);
                        Uri uri = Uri.parse(msg);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case LAYOUT_BOARD:

                break;
        }

        return view;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(ArrayList<SearchUtil> list) {
        if (list == null) {
            this.list = new ArrayList();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }

}
