/**
 * Created by Wezley on 1/23/16.
 */

import java.util.*;
import java.io.*;

public class main
{
    static Crawler pageCrawler = new Crawler();
    static Summer textSummer = new Summer();

    static Map summarizedPages = new HashMap<>();
    static Map alreadyVisitedUrls = new HashMap<>();


    static int currentIndexStep = 0;

    static private String startUrl = "https://en.wikipedia.org/wiki/Main_Page";


    public static void main(String[] args)
    {
        String currentPage = startUrl;
        while (currentIndexStep < 1000)
        {
            currentIndexStep++;
            if (currentPage != null)
            {
                String newPageInfo[] = pageCrawler.crawlCurrentPage(currentPage, alreadyVisitedUrls, currentIndexStep);
                alreadyVisitedUrls.put(currentIndexStep, currentPage);
                System.out.println(currentIndexStep + " " + currentPage);
                if(newPageInfo[1].length() > 1  )
                {
                    String pageSummary = textSummer.summarizeText(newPageInfo[1]);

                    if (!summarizedPages.containsKey(currentPage))
                    {
                        summarizedPages.put(currentPage, pageSummary);
                        System.out.println(pageSummary);
                    }
                }
                currentPage = newPageInfo[0];
            }
        }
    }
}
