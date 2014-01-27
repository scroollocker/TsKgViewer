package ru.skytechdev.tskgviewerreborn;

import java.io.File;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class TSEngine {
		private Menu mainMenu = new Menu();
		private Serials allInCat = new Serials();
		private SerialInfo serialInfo = new SerialInfo();
		private NewEpisodes newEpi = new NewEpisodes();
		private LastSeen lastSeen = new LastSeen();
		private Search searchEngine = new Search();
		private Favorites favorites = new Favorites();
		//private VideoUrlGenerator urlGen = new VideoUrlGenerator();
		private int seasonId;
		private int serialnId;
		private boolean isSelected;
		private boolean isMenuLoaded;
		private boolean isSerialSelect;
		private Context baseContext;
		
		public boolean isDefaultPlayer() {
			boolean result = true;
			SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(baseContext);
			result = mySettings.getBoolean("pref_defplayer", true);
			return result;
		}
		
		public ArrayList<String> makePlayList(int id) {
			ArrayList<String> epi = new ArrayList<String>();
			TSEpisodeItem item = serialInfo.getSerialItem(id);
			for(int i = 0; i < item.episodes.size();i++) {
				epi.add(getVideoUrl(item.episodes.get(i).url));
			}
			return epi;
		}
		
		public void clear() {
			allInCat.clear();
			serialInfo.clear();
			newEpi.clear();
			searchEngine.clear();
		}
		
		public int search(String text) {
			searchEngine.setCategories(mainMenu);
			searchEngine.search(text);
			return searchEngine.getItemsFound();
		}
		
		public String getSearchedCaption(int id) {
			return searchEngine.getSearchItem(id).value;
		}
		
		public String getSearchedUrl(int id) {
			return searchEngine.getSearchItem(id).url;
		}
		
		public String getSearchedImg(int id) {
			return searchEngine.getSearchItem(id).imgurl;
		}
		
		public int getSearchedCount() {
			return searchEngine.getItemsFound();
		}
		
		public Context getBaseContext() {
			return baseContext;
		}
		
		public void saveSettings() {
			lastSeen.saveToSettings();
		}
		
		public void init(Context main) {
			isMenuLoaded = mainMenu.parseMenu();
			isSelected = false;
			isSerialSelect = false;			
			baseContext = main;	
			lastSeen.setContext(baseContext);
			favorites.setContext(baseContext);
			lastSeen.loadFromSettings();
			favorites.loadFromSettings();
		}
		
		public int getFavoritesGetCount() {
			return favorites.getCount();
		}
		
		public TSItem getFavoritesItem(int id) {
			return favorites.getItem(id);
		}
		
		public boolean isInFavorites(String cap, String url, String img) {
			if (cap.isEmpty() || url.isEmpty() || img.isEmpty()) {
				return false;
			}
			TSItem item = new TSItem();
			item.imgurl = img;
			item.url = url;
			item.value = cap;
			return favorites.isExist(item);
		}
		
		public void delFromFavorites(String cap, String url, String img) {
			if (cap.isEmpty() || url.isEmpty() || img.isEmpty()) {
				return;
			}
			TSItem item = new TSItem();
			item.imgurl = img;
			item.url = url;
			item.value = cap;
			favorites.delete(item);
		}
		
		public void addToFavorites(String cap, String url, String img) {			
			TSItem item = new TSItem();
			item.imgurl = img;
			item.url = url;
			item.value = cap;
			favorites.add(item);
			favorites.saveToSettings();
		}
		
		public boolean getIsMenuLoaded() {
			return isMenuLoaded;
		}
		
		public int getSelectedSeasonId() {
			return seasonId;
		}
		
		public int getMenuItemCount() {
			return mainMenu.getItemCount();
		}
		
		public String getMenuItemCaption(int id) {
			return mainMenu.getItemById(id).value;
		}
		
		public boolean initNewEpi() {
			return newEpi.parseNewEpi();
		}
		
		public String getLastSeenCaption(int id) {
			return lastSeen.getCaption(id);
		}
		
		public String getLastSeenUrl(int id) {
			return lastSeen.getUrl(id);
		}
		
		public TSItem getLastSeenItem(int id) {
			return lastSeen.get(id);
		}
		
		public int getLastSeenCount() {
			return lastSeen.getCount();
		}
		
		public void addToLastSeen(String cap, String url, String img) {
			if (cap.isEmpty() || url.isEmpty()) {
				return;
			}
			TSItem item = new TSItem();
			item.value = cap;
			item.url = url;
			item.imgurl = img;
			lastSeen.add(item);
			lastSeen.saveToSettings();
		}
		
		public void selectCategory(int id) {
			if (!isMenuLoaded) {
				return;
			}
			String url = mainMenu.getItemById(id).url;
			if (!url.isEmpty()) {
				isSelected = allInCat.parseSerialsByUrl(url);				
			}
		}
		
		public void selectSerial(int id) {
			if (!isSelected) {
				return;
			}			
			String url = allInCat.getSerialById(id).url;
			selectSerial(url);
			if (isSerialSelect) {
				serialnId = id;
			}			
		}
		
		public void selectSerial(String url) {
			if (!url.isEmpty()) {
				isSerialSelect = serialInfo.parseSerialInfo(url);
				if (isSerialSelect) {					
					addToLastSeen(serialInfo.getCaption(),url,serialInfo.getImg());
					//urlGen.init(url,isDirectLink());
				}
			}				
		}
		
		public void selectSeason(int id) {
			if (!isSerialSelect) {
				return;
			}	
			seasonId = id;
		}
		
		public TSNewEpisodesItem getNewEpiItem(int id) {
			return newEpi.getEpiById(id);
		}
		
		public int getNewEpiCount() {
			return newEpi.getEpiCount();
		}
		
		public String getNewEpiCaption(int id) {
			return newEpi.getEpiById(id).caption;
		}
		
		public String getNewEpiUrl(int id) {
			return newEpi.getEpiById(id).link;
		}
		
		public String getNewEpiComment(int id) {
			return newEpi.getEpiById(id).comment;
		}
		
		public String getNewEpiDate(int id) {
			return newEpi.getEpiById(id).date;
		}
		
		public int getSerialsCount() {
			if (!isSelected) {
				return 0;
			}
			return allInCat.getSerialsCount();
		}
		
		public String getSerialCaption(int id) {
			if (!isSelected) {
				return "";
			}
			return allInCat.getSerialById(id).value;
		}
		
		public String getSerialCaption() {
			if (!isSerialSelect) {
				return "";
			}
			return serialInfo.getCaption();
		}
		
		public String getSerialUrl(int id) {
			if (!isSelected) {
				return "";
			}
			return allInCat.getSerialById(id).url;
		}
		
		public String getSerialImg(int Id) {
			if (!isSelected) {
				return "";
			}
			return allInCat.getSerialById(Id).imgurl;			
		}
		
		public String getSerialImg() {
			if (!isSerialSelect) {
				return "";
			}
			return serialInfo.getImg();
		}
		
		public String getSerialUrl() {
			if (!isSerialSelect) {
				return "";
			}
			return serialInfo.getUrl();
		}
		
		public String getSerialDiscription() {
			if (!isSerialSelect) {
				return "";
			}			
			
			return serialInfo.getDiscription();			
		}		
		
		public int getSeasonCount() {
			if (!isSerialSelect) {
				return 0;
			}
			return serialInfo.getSeasonCount();
		}
		
		public int getEpisodesCount(int id) {
			if (!isSerialSelect) {
				return 0;
			}
			return serialInfo.getEpisodesCount(id);
		}
		
		public String getSeasonCaption(int id) {
			if (!isSerialSelect) {
				return "";
			}
			return serialInfo.getSeasonCaption(id);
		}

		public String getEpisodeCaption(int episodeid) {
			if (!isSerialSelect) {
				return "";
			}
			return serialInfo.getEpisode(seasonId, episodeid).value;
		}	
		
		public String getVideoUrl(int episodeid) {
			if (!isSerialSelect) {
				return "";
			}
			String Url = serialInfo.getEpisode(seasonId, episodeid).url;
			if (Url.isEmpty()) {
				return "";
			}
			return getVideoUrl(Url);
		}		
		
		public String getVideoUrl(String url) {
			if (url.isEmpty()) {
				return "";
			}
			return url;//urlGen.makeVideoUrl(url);
		}
		
		
}
