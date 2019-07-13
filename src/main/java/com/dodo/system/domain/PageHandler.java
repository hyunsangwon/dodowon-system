package com.dodo.system.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Author Sangwon Hyun on 2019-07-12
 */
@Getter
@Setter
public class PageHandler {

    private int totalcount;//전체 게시물 개수
    private int pagenum;//현재 페이지 번호
    private int contentnum;//한페이지에 몇개 표시할지
    private int startPage;//현재 페이지 블록의 시작페이지
    private int endPage;//현재 페이지 블록의 마지막 페이지
    private boolean prev;//이전페이지
    private boolean next;//다음페이지
    private int currentblock;//현재 블록
    private int lastblock;//마지막 블록
    private int pageBlockCount=5; //한블록에 몇로우를 보여줄지 (ex: 1,2,3,4,5)

    public void prevnext(int pagenum) {

        if(pagenum > 0 && pagenum <=pageBlockCount && getLastblock() <= 1) {
            setPrev(false);
            setNext(false);
        }else if(pagenum > 0 && pagenum <=pageBlockCount) {
            setPrev(false); //첫번째 페이지 이므로
            setNext(true);
        }
        else if(getLastblock() == getCurrentblock()) {
            setPrev(true);
            setNext(false); //마지막 페이지 이므로
        }else {
            setPrev(true);
            setNext(true);
        }
    }

    /**
     * 총 보여줄 페이지 수
     * @param totalcount: 총 row 수,contentnum : 한페이징당 몇 row를 보여줄 것인지
     * @return int
     */
    public int calcpage(int totalcount, int contentnum) {
        /*
         * totalcount가 125개 이고  contentnum이  10이라고 가정하면, 125나누기 10 = 12.5
         * 12.5 에서 10을 나눴을때 나머지값이 0보다 크기 때문에 한페이지 더 ++ 해주면 총 13페이지
         */
        int totalpage = totalcount/contentnum;
        if(totalcount%contentnum > 0) {
            totalpage++;
        }
        return totalpage;
    }

    public void setStartPage(int currentblock) {
        //첫페이지 알아내기
        //1// (1 2 3 4 5) 한 블록
        //2// 6 7 8 9 10
        //3// 11 12 13
        this.startPage = (currentblock*pageBlockCount)-(pageBlockCount-1);
    }

    public void setEndPage(int getlastblock,int getcurrentblock) {
        //마지막 페이지를 알아내기 위해 calcpage통해 알아낸다.
        if(getlastblock == getcurrentblock) {
            this.endPage = calcpage(getTotalcount(),getContentnum());
        }
        else {
            this.endPage = getStartPage()+(pageBlockCount-1);
        }
    }

    public void setCurrentblock(int pagenum) {
        //페이지 번호를 통해서 구한다.
        //페이지 번호 / 페이지 그룹 안의 페이지 갯수
        // 1p 1/5 => 0.2 0 + 1 => 1 블록
        // 8p 8 /5 => 1.6 1 + 1 => 2 블록

        this.currentblock = pagenum/pageBlockCount;
        if(pagenum%pageBlockCount > 0) {
            this.currentblock++;
        }
    }

    public void setLastblock(int totalcount) {
        // 1~5 까지 있을때 1페이지당 10개의 row를 가져온다고 가정
        // 그러면 1~5까지의 row갯수는 50개가 나와야한다.
        // 전체 개시물이 125개가 있다고 가정할때 총 5페이만 보일 예정 즉 50개
        // 125/50 = 2.5
        // 페이지블록이  3!
        this.lastblock = totalcount / (pageBlockCount*this.contentnum);
        if(totalcount % (pageBlockCount*this.contentnum) > 0) {
            this.lastblock++;
        }
    }

}
