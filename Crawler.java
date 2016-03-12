/**
 * Created by Wezley on 1/23/16.
 */

import java.io.IOException;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler
{
    private String CRAWLER_AGENT = "Data Collection Crawler";
    private Document doc;
    private String nextUrl = "";
    int timesJumped = 2;

    public String[] crawlCurrentPage(String url, Map alreadyVisitedUrls, int indexStep)
    {
        try
        {
            Connection con = Jsoup.connect(url).userAgent(CRAWLER_AGENT);
            Document curDoc = con.get();
            this.doc = curDoc;
            int linkIndex = 0;
            String html = doc.body().toString();
            Document document = Jsoup.parse(html);
            Element link;
            boolean hasLink = false;
            while(!hasLink)
            {
                if (document.select("p").select("a").size() > linkIndex)
                {
                    link = document.select("p").select("a").get(linkIndex);
                }
                else if(document.select("a").size() > linkIndex)
                {
                    link = document.select("a").get(linkIndex);
                }
                else
                {
                    alreadyVisitedUrls.put(indexStep+1, url);
                    String newLink = alreadyVisitedUrls.get(indexStep-timesJumped).toString();
                    url = newLink;
                    con = Jsoup.connect(url).userAgent(CRAWLER_AGENT);
                    curDoc = con.get();
                    this.doc = curDoc;
                    linkIndex = 0;
                    System.out.println ("skip jump: " + url);
                    timesJumped++;
                    html = doc.body().toString();
                    document = Jsoup.parse(html);
                    continue;
                }

                String linkWithCaps = link.attr("href");
                String linkToScan = linkWithCaps.toLowerCase();
                if (!linkToScan.contains("//") && linkToScan.contains("/wiki/") &&
                        !linkToScan.contains("file") && !linkToScan.contains("special") && !linkToScan.contains(":"))
                {
                    nextUrl = "https://en.wikipedia.org" + linkWithCaps;
                    boolean alreadyVisited = pageExists(nextUrl, alreadyVisitedUrls);
                    String[] returnInfo = new String[3];

                    Elements text = curDoc.select("p");
                    String htmlText = text.text();
                    Document docText = Jsoup.parse(htmlText);
                    String textBody = docText.text();
                    returnInfo[0] = nextUrl;
                    returnInfo[1] = textBody;
                    returnInfo[2] = "" + linkIndex;

                    if(!alreadyVisited)
                    {
                        timesJumped = 2;
                        return returnInfo;
                    }
                    else
                    {
                        linkIndex++;
                    }
                }
                else
                {
                    linkIndex++;
                }
            }
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private boolean pageExists(String url, Map pages)
    {
        if(pages.containsValue(url))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
