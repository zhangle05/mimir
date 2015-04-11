package com.jotunheim.mimir.domain.wechat.response;

import java.util.List;

import com.jotunheim.mimir.domain.wechat.BaseMsg;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatNewsResMsg extends BaseMsg {

	private Integer ArticleCount;

	private List<Article> Articles;

	public WechatNewsResMsg() {
		super();
		MsgType = WX_MSG_TYPE_NEWS;
	}

	public Integer getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(Integer articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}

}
