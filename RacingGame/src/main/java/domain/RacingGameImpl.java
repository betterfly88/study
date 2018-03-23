package domain;

import common.Utils;
import dto.MOVING_TYPE;
import entity.CarMoveState;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by betterFLY on 2018. 3. 12.
 * Github : http://github.com/betterfly88
 */

public class RacingGameImpl extends RacingGame {
    private int cars, times;

    private Map<Integer, List<String>> racingCarList = new HashMap<>();
    private List <String> moveStateList;

    public RacingGameImpl(int cars, int times){
        this.cars = cars;
        this.times = times;
    }

    @Override
    public MOVING_TYPE getCarMovingStatement(int randNum) {
        if(randNum > 4 )
            return MOVING_TYPE.MOVE;

        return MOVING_TYPE.STOP;
    }

    @Override
    public String additionalMovementToString(MOVING_TYPE type) {
        if(moveStateList.size() < 1)
            return type.getType();

        return moveStateList.get(moveStateList.size()-1)+type.getType();
    }

    @Override
    public int stackUpMoveList(int times, List<String> moveStateList) {
        if(times < 1)
            return times;

        for(String moveState : moveStateList)
            moveStateList.add(additionalMovementToString(getCarMovingStatement(Utils.extractMoveNumber())));

        return stackUpMoveList(times-1, moveStateList);
    }

    public List<String> addToCarMoveList(){
        moveStateList = new ArrayList<>();
        IntStream
            .range(0,times)
            .forEach(i -> moveStateList.add(additionalMovementToString(getCarMovingStatement(Utils.extractMoveNumber()))));


        return moveStateList;
    }

    public void printList(){
        for(String print : addToCarMoveList())
            System.out.println(print);
    }

    @Override
    public int recursiveRacingGame(int cars) {
        if(cars < 1)
            return cars;

        moveStateList  = new ArrayList<>();
        stackUpMoveList(this.times, moveStateList);

        racingCarList.put(cars, moveStateList);
        return recursiveRacingGame(cars-1);
    }

    public Map<Integer, List<String>> settingCarMoveState(){
        IntStream.range(0,cars)
                .forEach(i ->racingCarList.put(i,addToCarMoveList()));
//        racingCarList.put(cars, addToCarMoveList());

        return racingCarList;
    }

    public void printMap(){
        IntStream.range(0, cars)
                .mapToObj(i -> settingCarMoveState())
                .forEach(System.out::println);

    }

    @Override
    public void start(){
        recursiveRacingGame(this.cars);
        for(int i=0; i<this.times; i++){
            for(int j=1; j<=this.cars; j++){
                System.out.println(i+1+" ROUND : "+racingCarList.get(j).get(i));
            }
            System.out.println();
        }
    }

    @Override
    public void stream(){
        recursiveRacingGame(this.cars);
        for(Map.Entry<Integer, List<String>> entry: racingCarList.entrySet()){

            System.out.println(entry.getKey()+" - "+ entry.getValue());

        }
    }
}