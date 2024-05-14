package JavProject;

//회원 정보 클래스
class Member {
 private long memberId;
 private int points;

 public Member(long memberId, int points) {
     this.memberId = memberId;
     this.points = 500; 
 }

 public long getMemberId() {
     return memberId;
 }

 public int getPoints() {
     return points;
 }


 // 누적 포인트를 증가시키는 메서드
 public void accumulatePoints(int points) {
     this.points += points;
 }
}


