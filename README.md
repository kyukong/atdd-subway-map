<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <a href="https://techcourse.woowahan.com/c/Dr6fhku7" alt="woowacourse subway">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/woowacourse/atdd-subway-map">
</p>

<br>

# 🚇 지하철 노선도 미션
스프링 과정 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started
### Usage
#### application 구동
```
./gradlew bootRun
```
<br>

## 👩‍💻 기능 요구 사항

### 지하철역

- [x] 지하철역을 등록한다.
  - [x] [예외처리] 동일한 이름의 지하철역을 등록할 경우 예외가 발생한다.
- [x] 지하철역의 목록을 조회한다.
- [x] 지하철역을 삭제한다.

### 지하철 노선

- [x] 지하철 노선을 등록한다.
  - [x] [예외처리] 동일한 이름의 지하철 노선을 등록할 경우 예외가 발생한다.
  - [x] [예외처리] 동일한 색상의 지하철 노선을 등록할 경우 예외가 발생한다.
  - [x] 지하철 구간을 등록한다.
- [x] 지하철 노선의 전체 목록을 조회한다.
- [x] 지하철 노선을 조회한다.
- [x] 지하철 노선을 수정한다.
  - [x] [예외처리] 동일한 이름의 지하철 노선으로 수정할 경우 예외가 발생한다.
  - [x] [예외처리] 동일한 색상의 지하철 노선으로 수정할 경우 예외가 발생한다.
- [x] 지하철 노선을 삭제한다.

### 지하철 구간

- [x] 지하철 구간을 등록한다.
  - [x] [예외처리] 상행역 아이디를 가지는 지하철역이 존재해야 한다.
  - [x] [예외처리] 하행역 아이디를 가지는 지하철역이 존재해야 한다.
  - [x] [예외처리] 구간거리는 `1` 이상이어야 한다.
  - [x] [예외처리] 상행역과 하행역이 이미 지하철 노선에 존재하면 등록할 수 없다.
  - [x] [예외처리] 추가하려는 구간이 노선에 포함되어 있지 않으면 등록할 수 없다.
  - [x] 상행 종점을 등록한다.
  - [x] 하행 종점을 등록한다.
  - [x] 상행 종점 또는 하행 종점을 등록할 때 갈래길이 생기지 않도록 구간 거리를 조정한다.
    - [x] [예외처리] 역 사이에 새로운 역을 추가할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없다.
- [x] 지하철 구간을 삭제한다.
  - [x] [예외처리] 노선에 구간이 하나만 존재할 경우 구간을 삭제할 수 없다.
  - [x] 중간역을 삭제할 경우 구간 거리는 두 구간 거리의 합이 된다.
  - [x] 종점이 제거될 경우 다음으로 오던 역이 종점이 된다.

### 프레임워크 적용

- [x] 스프링 JDBC 를 활용하여 H2 DB에 저장한다.
- [x] H2 DB를 통해 저장된 값을 확인한다.
- [x] 스프링 빈을 활용한다.

> 예외처리 시 IllegalArgumentException 이 발생한다.

<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-map/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-map/blob/master/LICENSE) licensed.
