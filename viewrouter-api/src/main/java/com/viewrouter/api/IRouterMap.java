package com.viewrouter.api;

import com.viewrouter.model.ViewInfo;

import java.util.Map;

public interface IRouterMap {
   public void loadInto(Map<String, ViewInfo> atlas);
}
