package com.su.searchlocaldemo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.su.searchlocaldemo.CharactorUtil.CharacterParser;
import com.su.searchlocaldemo.CharactorUtil.GetLetter;
import com.su.searchlocaldemo.CharactorUtil.ParseSortKey;
import com.su.searchlocaldemo.CharactorUtil.PinyinComparator;
import com.su.searchlocaldemo.Entity.AppInfo;
import com.su.searchlocaldemo.Entity.Contact;
import com.su.searchlocaldemo.Entity.MoreInfo;
import com.su.searchlocaldemo.Entity.SearchUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private EditText etSearch;
    private ImageView ivClearText;
    private TextView dialog;
    private LocalObjectAdapter adapter;

    private ArrayList<Contact> mAllContactsList;
    private ArrayList<AppInfo> mAllAppList;
    private ArrayList<SearchUtil> mAllInfoList = new ArrayList<>();
    private ArrayList<SearchUtil> mShowList = new ArrayList<>();//维护的显示List
    //汉字转换成拼音的类
    private CharacterParser characterParser;
    // 根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator;
    private ArrayList<SearchUtil> fileterList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        loadContacts();

    }

    private void initView() {
        dialog = (TextView) findViewById(R.id.dialog);
        ivClearText = (ImageView) findViewById(R.id.ivClearText);
        etSearch = (EditText) findViewById(R.id.et_search);
        mListView = (ListView) findViewById(R.id.lv_contacts);

        /** 给ListView设置adapter **/
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        adapter = new LocalObjectAdapter(this, mAllInfoList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (mShowList.get(i).setEntity() == 3) {
                    adapter = new LocalObjectAdapter(MainActivity.this, fileterList);//实现展开全部的搜索结果
                    mListView.setAdapter(adapter);
                    // adapter.updateListView(fileterList);//这里有疑问，为什么不能直接update？？？
                }
            }
        });
    }

    private void initListener() {
        /**清除输入字符**/
        ivClearText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {

                //TODO
                String content = etSearch.getText().toString();
                if ("".equals(content)) {
                    ivClearText.setVisibility(View.INVISIBLE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
                if (content.length() > 0) {
                    //ArrayList<SearchUtil> fileterList = (ArrayList<SearchUtil>) search(content);//局部变量不会出现清空的状况
                    if (fileterList != null) {
                        fileterList.clear();
                    }

                    FuzzySearch fzSearch=new FuzzySearch(mAllContactsList,mAllAppList);
                    fileterList = (ArrayList<SearchUtil>) fzSearch.search(content);

                    if (fileterList.size() > 3) {
                        MoreInfo board = new MoreInfo(null, null, null);
                        mShowList.add(fileterList.get(0));
                        mShowList.add(fileterList.get(1));
                        mShowList.add(board);
                    } else {
                        mShowList.addAll(fileterList);
                    }
                    adapter.updateListView(mShowList);

                } else {
                    if (mShowList != null) {
                        mShowList.clear();
                        //mShowList.removeAll(mShowList);//有说clear过期的
                    }
                    adapter = new LocalObjectAdapter(MainActivity.this, mAllInfoList);
                    mListView.setAdapter(adapter);
                    // adapter.updateListView(mAllInfoList);
                }
                mListView.setSelection(0);

            }

        });


    }

    private void loadContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//Phone.CONTENT_URI
                    ContentResolver resolver = getApplicationContext().getContentResolver();
                    Cursor phoneCursor = resolver.query(uri, new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key"}, null, null, "sort_key COLLATE LOCALIZED ASC");
                    if (phoneCursor == null || phoneCursor.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "未获得读取联系人权限 或 未获得联系人数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");

                    if (phoneCursor.getCount() > 0) {
                        mAllContactsList = new ArrayList<Contact>();
                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                            if (TextUtils.isEmpty(phoneNumber))
                                continue;
                            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                            String sortKey = phoneCursor.getString(SORT_KEY_INDEX);
                            Contact sortModel = new Contact(contactName, phoneNumber, sortKey);
                            String sortLetters = new GetLetter(characterParser, contactName).getSortLetter();
                            sortModel.sortLetters = sortLetters;
                            sortModel.sortToken = new ParseSortKey(characterParser, sortKey).parseSortKey();

                            mAllInfoList.add(sortModel);
                            mAllContactsList.add(sortModel);
                        }
                    }
                    phoneCursor.close();

                } catch (Exception e) {
                    Log.e("xbc", e.getLocalizedMessage());
                }finally {
                    LoadAppInfo loadAppInfo=new LoadAppInfo(MainActivity.this,characterParser);
                    mAllAppList=loadAppInfo.getAppInfo();
                    mAllInfoList.addAll(mAllAppList);
                     runOnUiThread(new Runnable() {
                        public void run() {
                            //  Collections.sort(mAllInfoList, pinyinComparator); 放弃对整个的排序，确定将联系人信息放在前面
                            adapter.updateListView(mAllInfoList);
                        }
                    });
                  //  adapter.updateListView(mAllInfoList);
                }
            }
        }).start();
    }




}
