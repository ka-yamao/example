package c.local.com.example.data;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class PokemonListInfo {
	public int count;
	public boolean isNext;
	public String limit, offset;

	public PokemonListInfo() {
		this.count = 0;
		this.isNext = true;
		this.limit = "20";
		this.offset = "0";
	}

	public PokemonListInfo(int count, String nextUrl) {
		this.count = count;
		if (nextUrl == null) {
			this.isNext = false;
		} else {
			this.isNext = true;
			this.limit = Uri.parse(nextUrl).getQueryParameter("limit");
			this.offset = Uri.parse(nextUrl).getQueryParameter("offset");
		}
	}
	public Map<String,String> toQueryMap(){
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("limit", this.limit);
		queryMap.put("offset", this.offset);
		return queryMap;
	}
}
