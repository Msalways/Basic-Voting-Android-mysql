package com.bVote.basicvoting.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String Dbname = "Voting System";
    private static final int DbVersion = 1;

    public DbHelper(@Nullable Context context) {
        super(context, Dbname, null, DbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String user = "create table user(userId Text primary key," +
                "name text," +
                "state text," +
                "region text," +
                "password text)";
        db.execSQL(user);

        String votes = "create table vote(name text," +
                "party text,"+
                "state text," +
                "region text," +
                "votes integer," +
                "primary key(name,party,region,state))";
        db.execSQL(votes);

        String visited = "create table visited(name text)";
        db.execSQL(visited);
    }

    public void insUser(String userId, String name, String region, String state, String password){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userId",userId);
        values.put("name",name);
        values.put("state",state);
        values.put("region",region);
        values.put("password",password);
        db.insert("user",null,values);
    }

    public void insCandidates(String name,String party,String state, String region){
        SQLiteDatabase db = getWritableDatabase();
        final int votes = 0;
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("party",party);
        values.put("state",state);
        values.put("region",region);
        values.put("votes",votes);
        db.insert("vote",null,values);
    }

    public void insVisited(String name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        db.insert("visited",null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if Exists user");
        db.execSQL("drop table if Exists vote");
        db.execSQL("drop table if Exists visited");
        onCreate(db);
    }

    public boolean loginCheck(String voterId, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        boolean checkVisitedUsers = checkVisited(voterId);
        Log.d("zz", "loginCheck: "+checkVisitedUsers);
        insVisited(voterId);
        @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from user where userId = '" + voterId + "' and password = '" + pass + "' ", null);
        Log.d("zz", "loginCheck: " + voterId + " " + pass);
        Log.d("zz", " on voter: " + String.valueOf(res.moveToFirst()));
        return res.moveToFirst();
    }

    public boolean checkVisited(String name){
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select * from visited where name = '"+name+"'",null);
        LogVisited();
        Log.d("zz", "checkVisited: "+String.valueOf(res.moveToFirst()));
        return res.moveToFirst();
    }

    private void LogVisited() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor res = db.rawQuery("select * from visited",null);
        res.moveToFirst();
        Log.d("zz", "LogVisited: "+res.moveToFirst());
    }

    public ArrayList<String> FetchCandidates(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        String state, region;
        state = getLocOfUser("state",userId);
        region = getLocOfUser("region",userId);
        ArrayList<String> candidates = new ArrayList<>(),party = new ArrayList<>();
        ArrayList<String> mergedCandi = new ArrayList<>();
        String candi = "select name,party from vote where state = '"+state+"' and region = '"+region+"'";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(candi,null);
        cursor.moveToFirst();
        Log.d("zz"," on candi "+ cursor.moveToFirst());
        Log.d("zz", "FetchCandidates: "+state+" "+region);
//        Log.d("zz", "FetchCandidates: "+cursor.getString(cursor.getColumnIndex("name")));
        while (!cursor.isAfterLast()){
            candidates.add(cursor.getString(cursor.getColumnIndex("name")));
            party.add(cursor.getString(cursor.getColumnIndex("party")));
            cursor.moveToNext();
        }
        int len = candidates.size();
        for (int i = 0; i < len; i++){
            String temp = candidates.get(i)+"-"+party.get(i);
            mergedCandi.add(temp);
        }
        return mergedCandi;
    }

    private String getLocOfUser(String loc,String userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select * from user where userId = '"+userId+"'",null);
        res.moveToFirst();
        Log.d("zz", "getLocOfUser: "+loc);
        return res.getString(res.getColumnIndex(loc));
    }

    public ArrayList<String> ShowUsers() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> ShowUserId = new ArrayList<>();
        ArrayList<String> ShowUserName = new ArrayList<>();
        ArrayList<String> ShowCombined = new ArrayList<>();
        Cursor res = db.rawQuery("select * from user",null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            ShowUserId.add(res.getString(res.getColumnIndex("userId")));
            ShowUserName.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        int size = ShowUserId.size();
        for (int i=0;i<size;i++){
            String temp = ShowUserId.get(i)+" - "+ShowUserName.get(i);
            ShowCombined.add(temp);
        }
        Log.d("from db", String.valueOf(ShowCombined));
        return ShowCombined;
    }

    public boolean DeleteUsers(String uid, String uname) {
        SQLiteDatabase db = getWritableDatabase();
        boolean check = db.delete("user", "userId=? and name=? ", new String[]{uid,uname}) > 0;
        Log.d("Show", String.valueOf(check));
        return check;
    }

    public boolean DeleteCandidates(String cname, String cparty) {
        SQLiteDatabase db = getWritableDatabase();
        boolean check = db.delete("vote", "name=? and party=? ", new String[]{cname,cparty}) > 0;
        Log.d("Show delCandi", String.valueOf(check));
        return check;
    }

    public ArrayList<String> ShowCandi() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> ShowCandidateName = new ArrayList<>();
        ArrayList<String> ShowCandidateParty = new ArrayList<>();
        ArrayList<String> ShowCombined = new ArrayList<>();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from vote",null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            ShowCandidateName.add(res.getString(res.getColumnIndex("name")));
            ShowCandidateParty.add(res.getString(res.getColumnIndex("party")));
            res.moveToNext();
        }
        int size = ShowCandidateName.size();
        for (int i=0;i<size;i++){
            String temp = ShowCandidateName.get(i)+" - "+ShowCandidateParty.get(i);
            ShowCombined.add(temp);
        }
        return ShowCombined;
    }

    public void AddVoteTo(String s) {
        SQLiteDatabase db = getWritableDatabase();
        String getVote = getVoteOfCandidate(s);
        int getVoteInteger  = Integer.parseInt(getVote);
        int updateVote = getVoteInteger + 1;
        String sql = "update vote set votes = "+updateVote+" where name = '"+s+"' ";
        db.execSQL(sql);
    }

    private String getVoteOfCandidate(String s) {
        String votes = "";
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from vote where name = '"+s+"'";
        Cursor res = db.rawQuery(sql,null);
        if (res.moveToFirst()){
            votes = res.getString(res.getColumnIndex("votes"));
            Log.d("zz", "getVoteOfCandidate: "+votes);
        }
        return votes;
    }

    public ArrayList<String> getResultVotes(){
        ArrayList<String> getName = new ArrayList<>();
        ArrayList<String> getVote = new ArrayList<>();
        ArrayList<String> getParty = new ArrayList<>();
        ArrayList<String> getDetails = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select * from vote",null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            getName.add(res.getString(res.getColumnIndex("name")));
            getParty.add(res.getString(res.getColumnIndex("party")));
            getVote.add(res.getString(res.getColumnIndex("votes")));
            res.moveToNext();
        }
        for (int i=0;i<getName.size();i++){
            String finalOut = getName.get(i)+" - "+getParty.get(i)+" - "+getVote.get(i);
            getDetails.add(finalOut);
        }
        return getDetails;
    }


    public void removeVisited() {
       SQLiteDatabase db = getWritableDatabase();
       LogVisited();
       db.delete("visited",null,null);
    }
}
