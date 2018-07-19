package com.emjiayuan.app.entity;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.entity.Province;

public class Global {
    public static LoginResult loginResult;
    public static String location_name="";
    public static String provinceid="0";
    public static String device_no="";
    public static String token="";
    public static ArrayList<Product> list;
    public static ArrayList<Province> datas;
    public static ArrayList<Products> Productslist;
    public static List<AreaModel> city_list=new ArrayList<>();
    public static List<CityBean> city_list2=new ArrayList<>();
    public static List<String> images;
}
