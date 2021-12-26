# 아이템 1. 생성자 대신 정적 팩터리 메서드를 고려하라.



## 1. 정적 팩토리 메서드 패턴

- 직접적으로 생성자를 통해 객체를 생성하지 않고 정적 메서드를 통해 객체를 생성하는 방식을 의미한다.

  → ex) LocalDateTime.of(int hour, int minute), Enum.vlaueOf(String arg)

- 생성자의 접근 제한자를 private으로 변경할 수 있다. 즉, 정적 팩토리 메서드를 통해 객체 생성을 제한할 수 있다.

<br>

## 2. 정적 팩터리 메서드 사용의 이점

### 1. 이름을 가질 수 있다.

- 정적 팩토리는 객체의 특성과 생성 목적의 의미를 부여할 수 있는 장점이 있다.

  → BigInteger.probalbePrime : 값이 소수인 BigInteger를 반환한다는 의미

  - 매개변수 타입과 갯수가 같은 생성자를 여러개 만들 수 있다.

    ```java
    public class Book {
      private String name;
      private String author;
      
      public Book (String name) {
        this.name = name;
      }
      
      //불가능
      public Book (String author) {
        this.author = author;
      }
    }
    ```

    ```Java
    public class Book {
      private String name;
      private String author;
    
      public static Book createBookWithName(String name) {
        Book book = new Book();
        book.name = name;
        return book;
      }
      
      public static Book createBookWithAuthor(String author) {
        Book book = new Book();
        book.author = author;
        return book;
      }
    }
    ```

    출처 : https://github.com/Meet-Coder-Study/book-effective-java/blob/main/2%EC%9E%A5/1_%EC%83%9D%EC%84%B1%EC%9E%90_%EB%8C%80%EC%8B%A0_%EC%A0%95%EC%A0%81%20%ED%8C%A9%ED%84%B0%EB%A6%AC_%EB%A9%94%EC%84%9C%EB%93%9C%EB%A5%BC_%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC_%EA%B9%80%EB%AF%BC%EA%B1%B8.md

<br>

### 2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.

- 인스턴스 캐싱과 인스턴스 통제가 가능하다.

1. `인스턴스 캐싱`
   - 미리 생성한 인스턴스를 필요할 때마다 사용하는 방식을 말한다.
   - 인스턴스를 캐싱 및 재활용하므로 불필요한 객체 생성을 피하고 객체 생성의 비용을 줄인다.
   - 같은 객체를 자주 요청하는 상황의 성능을 향상 시킨다. ex) Enum.valueOf(String args)
2. `인스턴스 통제`
   - 해당 인스턴스의 생성주기를 통제하는 것을 의미한다.
   - 인스턴스를 통제하는 이유
     1. 싱글톤을 만들기 위해
     2. 인스턴스화 불가로 만들기 위해
     3. 불변 클래스에서 값이 같은 인스턴스를 단 하나임을 보장하기 위해

<br>

### 3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

- 정적 팩터리 메서드의 리턴 타입을 인터페이스로 할 경우, 하위 타입 객체를 반환할 수 있다.

  > `동반 클래스`
  >
  > - 자바 1,8 이전에는 인터페이스에 정적 메서드를 선언할 수 없었다.
  >
  > - 따라서 인터페이스 기능을 추가하기 위해 동반 클래스를 만들어 그 안에 정적 메서드를 추가했다.
  > - In java) Collection의 동반 클래스 : **Collections**

<br>

`Collections.unmodifiableList` :  리스트의 부가적인 추가 삭제를 막기위한 메서드

```java
public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return (list instanceof RandomAccess ?
                new UnmodifiableRandomAccessList<>(list) :
                new UnmodifiableList<>(list));
}
```

- 이런 팩토리 메서드를 통해 메서드의 반한 타입은 List이지만 실제로는 List의 하위 객체를 반환할 수 있다.
- 이렇게 팩토리 메서드를 사용하면 해당 인터페이스의 구현체를 일일이 알아볼 필요가 없다.

<br>

### 4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

`EnumSet.noneof` : universe.length에 따라 다른 리턴타입을 반한하는 함수.

```java
public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType) {
        Enum<?>[] universe = getUniverse(elementType);
        if (universe == null)
            throw new ClassCastException(elementType + " not an enum");

        if (universe.length <= 64)
            return new RegularEnumSet<>(elementType, universe);
        else
            return new JumboEnumSet<>(elementType, universe);
    }
```

- 팩토리 메서드를 사용하면 사용자가 내부 구현에 대해 알 필요 없이 원하는 반환값을 전달받을 수 있다.
- 또한 메서드 내부 구현이 변경되어도, 반환타입만 같다면 사용자에게 영향을 끼치지 않는다.

<br>

## 3. 정적 팩터리 메서드 단점

### (1). 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.

- 상속을 위해서는 하위 클래스에서 사용할 수 있는 생성자가 필요하다.
- private 생성자를 통해 외부 생성을 막고, 정적 팩터리 메서드만을 사용할 경우, 하위 클래스를 만들 수 없다.

<br>

### (2) 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.

- 생성자의 이름이 클래스 이름과 같아서 명확히 알 수 있다.
- 반면, 정적 팩터리 메서드는 다른 메서드와 섞여 찾기 어려울 수 있다.

<br>

## 4. 정적 팩터리 메서드 네이밍

- `from` : 매개변수를 하나 받아, 해당 인스턴스를 반환하는 형변환 메서드

  ```java
  // instant 타입을 받아 Date로 변환하는 함수
  Date d = Date.from(instant);
  ```

- `of` : 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드

  ```java
  Set<Rank> faceCard = EnumSet.of(JACK, QUEEN, KING);
  ```

- valueOf : from과 of의 더 자세한 버전

- Instance / getInstance : 인스턴스를 반환하지만 같은 인스턴스임을 보장하지 않는다.

- create / newInstance : 매번 새로운 인스턴스를 반환함을 보장하는 함수

- getXXX : getInstance와 같으나,생성할 클래스가 아닌 다른 클래스의 팩토리 메서드를 정의할 때 사용.

  ```java
  FileStore fs = Files.getFileStore(path);
  ```

- newXXX : newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스의 팩토리 메서드를 정의할 때 사용.

  ```java
  BufferedReader br = Files.newBufferedReader(path);
  ```

- xxx : getXXX과 newXXX의 간결한 버전

  ```java
  List<Complaint> litany = Collections.list(legacyListany);
  ```

<br>

## Reference

- [tecoble] 정적 팩토리 메서드(Static Factory Method)란? : https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/
- [Meet-Coder-Study] book-effective-java : https://github.com/Meet-Coder-Study/book-effective-java