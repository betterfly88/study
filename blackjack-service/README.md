# BLACK JACK 
## with Test Driven Development (feat.oop)

![blackjack](img/blackjack.jpg)

### [블랙잭 게임](http://www.247blackjack.com/)

### 규칙
- 카드의 숫자 계산은 숫자에 써있는 그대로. 
- 카드의 합이 **21점** 또는 21점에 가장 가까운 사람이 이기는 게임
- K,Q,J는 모두 10점
- 에이스는 1점 혹은 11점 중 유리한 숫자로 계산할 수 있음
- 카드는 총 52장
    - Suits : Spade, Heart, Diamond, Club
    - Type : Ace, 2~10, Jack, Queen, King
- 처음 카드 2장이 주어짐. (처음 2장의 카드가 에이스와 10(J,Q,K 포함)으로 합이 21점이 되면 '블랙잭')
- 블랙잭인 경우 배팅액의 2배 획득
- 배탱액 : 배당율은 건 금액 만큼 버는 것이 기본. (플레이어가 두명인 경우 100원을 걸어서 이기면 200원, 지면 100원을 잃음)
- 게이머는 얼마든지 카드를 추가로 뽑을 수 있다.(Hit)
- 딜러는 2카드의 합계 점수가 16점 이하이면 반드시 1장을 추가로 뽑고, 17점 이상이면 추가할 수 없다.(정식 룰)
- 양쪽다 추가 뽑기 없이, 카드를 오픈하면 딜러와 게이머 중 소유한 카드의 합이 21에 가장 가까운 쪽이 승리한다.
- 단, 21을 초과하면 초과한 쪽이 진다.(Bust)
    - 딜러의 점수와 비교해서 동점이면 무승부, 
    - 딜러보다 높으면 이기고 낮으면 지게 된다. 
    - 딜러/플레이어, 둘 다 버스트면 딜러가 이긴다.




### TDD 기본 요구사항

1. 한 메서드에 오직 한 단계의 들여쓰기(indent)만 한다.
1. else 예약어를 쓰지 않는다.
1. 모든 원시값과 문자열을 포장한다.
1. 모든 엔티티를 작게 유지한다.
1. 2개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
1. 일급 콜렉션을 쓴다.
1. 한 줄에 점을 하나만 찍는다.
1. 줄여쓰지 않는다(축약 금지).
1. 게터/세터/프로퍼티를 쓰지 않는다.



## Test Case
[카드]
- 전체 카드가 1장씩 있는지 검증한다 (52장)

[게임진행]
- 금액을 배팅한다
    - 배팅한 금액이 내 잔액보다 작아야 함
- 각 플레이어는 임의의 2장의 카드를 받는다
    - 받은 카드의 합이 21인 경우 Black Jack (10,J,Q,K + Ace)
- 딜러가 뽑은 카드 중 임의의 카드 1장을 보여준다
**딜러기준**
    - 딜러는 카드의 합이 16점 이하면 반드시 1장의 카드를 추가로 뽑는다
    - 딜러는 갖고 있는 카드가 17이상이면 뽑을지 말지를 랜덤으로 자동 기능
    

**플레이어기준**
    - 딜러의 카드를 보고 Hit/Stand를 결정한다
    - Hit한 경우 임의의 카드를 한장 더 가져온다
        - 가져온 카드까지의 합이 21보다 작으면 Hit/Stand 기회를 또 준다
        - 가져온 카드까지의 합이 21보다 크면 Bust

- 에이스(Ace)가 있을 경우 
    - Ace+다른 카드 > 21보다 크면 Ace == 1
    - Ace+다른 카드 < 21보다 크면 Ace == 11
    - Ace가 2장 이상인 경우 무조건 1
        
- 플레이어가 Stand 또는 Bust 한 경우 딜러와 패를 비교한다
    - 21보다 작고 21에 가까운 쪽이 승리한다
    - 둘다 Bust인 경우 딜러가 승리한다
    - 이긴 쪽은 상대의 배팅액을 가져간다
    - 플레이어는 해당 배팅액 만큼 +- 한다