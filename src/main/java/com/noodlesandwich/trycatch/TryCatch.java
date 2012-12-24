package com.noodlesandwich.trycatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class TryCatch {
    private final Action action;
    private final Collection<TypedExceptionHandler> exceptionHandlers;

    private TryCatch(Action action, Collection<TypedExceptionHandler> exceptionHandlers) {
        this.action = action;
        this.exceptionHandlers = exceptionHandlers;
    }

    public static TryCatch tryDoing(Action action) {
        return new TryCatch(action, Collections.<TypedExceptionHandler>emptyList());
    }

    public <E extends Throwable> TryCatch catching(Class<E> exceptionType, ExceptionHandler<E> exceptionHandler) {
        List<TypedExceptionHandler> newExceptionHandlers = new ArrayList<>(exceptionHandlers);
        newExceptionHandlers.add(new TypedExceptionHandler(exceptionType, exceptionHandler));
        return new TryCatch(action, newExceptionHandlers);
    }

    public void run() throws Throwable {
        try {
            action.run();
        } catch (Throwable t) {
            exceptionHandlers.stream()
                    .filter((exceptionHandler) -> exceptionHandler.canHandle(t))
                    .findFirst()
                    .orElseThrow(() -> t)
                    .handle(t);
        }
    }

    public static interface Action {
        void run() throws Throwable;
    }

    public static interface ExceptionHandler<E extends Throwable> {
        void handle(Throwable exception) throws Throwable;
    }

    public static final class TypedExceptionHandler {
        private final Class<?> type;
        private final ExceptionHandler<?> handler;

        public <E extends Throwable> TypedExceptionHandler(Class<E> exceptionType, ExceptionHandler<E> exceptionHandler) {
            this.type = exceptionType;
            this.handler = exceptionHandler;
        }

        public boolean canHandle(Throwable exception) {
            return type.isInstance(exception);
        }

        public void handle(Throwable t) throws Throwable {
            handler.handle(t);
        }
    }
}
