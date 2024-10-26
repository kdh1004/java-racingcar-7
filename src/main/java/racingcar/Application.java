package racingcar;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        // 프로그램 시작
        System.out.println("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)");
        List<String> carNames = getCarNames(); // 1번 기능: 자동차 이름 입력 및 검증
        System.out.println("입력된 자동차 이름: " + carNames); // 입력된 자동차 이름 출력 (디버깅 용도)
        
        int attempts = getAttempts(); // 2번 기능: 시도할 횟수 입력 및 검증
        System.out.println("입력된 시도 횟수: " + attempts); // 입력된 시도 횟수 출력 (디버깅 용도)
        
        List<Car> cars = initializeCars(carNames); // 3번 기능: 자동차 객체 생성 및 초기화
        System.out.println("초기화된 자동차: " + cars); // 초기화된 자동차 출력 (디버깅 용도)

        race(cars, attempts); // 4번 기능: 경주 진행
        printWinners(cars); // 5번 기능: 우승자 출력
    }

    /**
     * 사용자로부터 자동차 이름을 입력받고 유효성을 검사하는 메서드
     * @return 유효성 검사를 통과한 자동차 이름 목록
     */
    private static List<String> getCarNames() {
        String input = Console.readLine(); // 자동차 이름 입력
        String[] names = input.split(","); // 쉼표를 기준으로 이름 분리
        
        List<String> carNames = new ArrayList<>(); // 유효성 검사
        for (String name : names) {
            name = name.trim();  // 이름 앞뒤 공백 제거
            
            if (name.isEmpty() || name.length() > 5) {
                throw new IllegalArgumentException("자동차 이름은 빈 값이거나 5자를 초과할 수 없습니다: " + name); // 이름이 빈 값이거나 5자를 초과할 경우 예외 발생
            }
            
            carNames.add(name);
        }
        
        return carNames;
    }

    /**
     * 사용자로부터 시도할 횟수를 입력받고 유효성을 검사하는 메서드
     * @return 유효성 검사를 통과한 시도 횟수
     */
    private static int getAttempts() {
        System.out.println("시도할 횟수는 몇 회인가요?");
        String input = Console.readLine(); // 시도 횟수 입력
        
        try {
            int attempts = Integer.parseInt(input); // 문자열을 정수로 변환
            if (attempts <= 0) {
                throw new IllegalArgumentException("시도 횟수는 1 이상의 정수여야 합니다.");
            }
            return attempts;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("잘못된 숫자 형식입니다. 시도 횟수는 정수여야 합니다.");
        }
    }

    /**
     * 자동차 이름 목록을 이용해 Car 객체를 초기화하는 메서드
     * @param carNames 자동차 이름 목록
     * @return 생성된 Car 객체 목록
     */
    private static List<Car> initializeCars(List<String> carNames) {
        List<Car> cars = new ArrayList<>();
        for (String name : carNames) {
            cars.add(new Car(name)); // 각 자동차 이름을 사용하여 Car 객체 생성
        }
        return cars;
    }

    /**
     * 주어진 시도 횟수만큼 경주를 진행하며 각 라운드의 결과를 출력하는 메서드
     * @param cars 경주에 참가하는 자동차 목록
     * @param attempts 경주 시도 횟수
     */
    private static void race(List<Car> cars, int attempts) {
        System.out.println("실행 결과"); // 경주 시작 알림
        for (int i = 0; i < attempts; i++) {
            playRound(cars); // 각 라운드마다 자동차를 움직임
            printRoundResult(cars); // 라운드 결과 출력
        }
    }

    /**
     * 각 라운드에서 자동차가 전진할지 멈출지 결정하는 메서드
     * @param cars 경주에 참가하는 자동차 목록
     */
    private static void playRound(List<Car> cars) {
        for (Car car : cars) {
            int randomNumber = Randoms.pickNumberInRange(0, 9); // 0에서 9 사이의 무작위 값 생성
            if (randomNumber >= 4) {
                car.move(); // 무작위 값이 4 이상일 경우 자동차 전진
            }
        }
    }

    /**
     * 각 라운드가 끝난 후 자동차의 현재 상태를 출력하는 메서드
     * @param cars 경주에 참가하는 자동차 목록
     */
    private static void printRoundResult(List<Car> cars) {
        for (Car car : cars) {
            System.out.println(car); // 각 자동차의 현재 상태 출력
        }
        System.out.println(); // 라운드 간 줄바꿈
    }

    /**
     * 경주가 끝난 후 우승자를 출력하는 메서드
     * @param cars 경주에 참가한 자동차 목록
     */
    private static void printWinners(List<Car> cars) {
        int maxPosition = cars.stream().mapToInt(Car::getPosition).max().orElse(0); // 최대 위치 확인
        List<String> winners = cars.stream()
                .filter(car -> car.getPosition() == maxPosition) // 최대 위치에 있는 자동차 필터링
                .map(Car::getName)
                .collect(Collectors.toList());

        System.out.println("우승자: " + String.join(", ", winners)); // 우승자 이름 출력
    }
}

/**
 * Car 클래스는 자동차 이름과 현재 위치를 저장하는 객체를 나타낸다.
 */
class Car {
    private final String name;
    private int position;

    public Car(String name) {
        this.name = name;
        this.position = 0; // 초기 위치는 0
    }

    public String getName() {
        return name; // 자동차의 이름을 반환
    }

    public int getPosition() {
        return position; // 자동차의 위치를 반환
    }

    public void move() {
        position++; // 자동차가 전진했을 때 위치를 증가시키는 메서드
    }

    @Override
    public String toString() {
        return name + " : " + "-".repeat(position); // 자동차의 현재 상태를 문자열로 반환 (디버깅 및 출력 용도)
    }
}
