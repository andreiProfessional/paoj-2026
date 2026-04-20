package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayDeque;
import java.util.Deque;

public class Order {

    private OrderState currentState;
    private final Deque<OrderState> history = new ArrayDeque<>();

    public Order(OrderState initialState) {
        this.currentState = initialState;
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        OrderState next = currentState.next();
        history.push(currentState);
        currentState = next;
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (currentState.isFinal()) {
            throw new CannotCancelFinalOrderException("Cannot cancel an order that is already in a final state: "
                    + currentState);
        }
        history.push(currentState);
        currentState = OrderState.CANCELED;
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException("No previous state to revert to.");
        }
        currentState = history.pop();
    }
}
