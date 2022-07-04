## 11.4 SELECT

### 1. SELECT 질의 순서

```SQL
SELECT s.emp_no, COUNT(DISTINCT e.first_name) AS cnt
FROM salaries s
INNER JOIN employees ON emp_no=s.emp_no
WHERE s.emp_no IN (100001, 100002)
GROUP BY s.emp_no
HAVING AVG(s.salary) > 1000
ORDER BY AVG(s.salary)
LIMIT 10;
```

1. SELECT
2. FROM
3. JOIN
4. WHERE
5. GROUP BY
6. HAVING
7. ORDER BY
8. LIMIT

<br>

### 2. WHERE 절과 GROUP BY 절, ORDER BY 절의 인덱스 사용

### 1. 인덱스를 사용하기 위한 기본 규칙

- WHERE, ORDER BY, GROUP BY 에서 인덱스를 사용하기 위해서는 <u>값 자체를 그대로 사용한다는 조건</u>을 만족해야 한다.
- 인덱스는 <u>칼럼 값을 아무런 변환 없이 B-Tree에 정렬해서 저장</u>한다.
- WHERE 절에 사용되는 비교 조건에서 <u>연산자 양쪽의 두 비교 대상의 값은 데이터 타입이 일치</u>해야 한다.

<br>

### 2. WHERE 절에서 인덱스 사용

- WHERE 조건절에 나열된 순서가 인덱스와 다르더라도 옵티마이저는 인덱스를 사용할 수 있는 조건들을 뽑아서 최적화를 수행한다. (WHERE절은 순서는 상관없다.)
- WHERE 조건절에서는 OR 연산자를 사용하지 않도록 한다. (OR 연산자 대신에 **`IN 연산자`** 를 사용)

### 3. GROUP BY

- GROUP BY 절에서는 명시된 칼럼의 순서와 인덱스를 구성하는 컬럼의 순서와 같아야 인덱스를 사용할 수 있다.
- ex) Index 설정 칼럼 순서 : COL_1, COL_2, COL_3, COL_4

`불가능`
```SQL
GROUP BY COL_2, COL_1
GROUP BY COL_1, COL_3
GROUP BY COL_1, COL_2, COL_3, COL_4, COL_5
```

`가능`
```sql
... WHERE COL_1 = '상수' ... GROUP BY COL_2, COL_3
... WHERE COL_1 = '상수' AND COL_2 ... GROUP BY COL_3, COL_4
```

<br>

### 4. ORDER BY

- ORDER BY 사용 조건 : GROUP BY에서 사용하는 조건 + 오름차순, 내림차순 옵션이 인덱스와 같거나 정반대인 경우에만 사용 가능
- ex) INDEX 설정 컬럼 순서 : COL_1 ASC, COL_2 ASC, COL_3 ASC

`불가능`
```sql
ORDER BY COL_2, COL_3
ORDER BY COL_1, COL_2 DESC, COL_3
ORDER BY COL_1, COL_3
```

`가능`
```sql
ORDER BY COL_1, COL_2
ORDER BY COL_1 DESC, COL_2 DESC, COL_3 DESC
```