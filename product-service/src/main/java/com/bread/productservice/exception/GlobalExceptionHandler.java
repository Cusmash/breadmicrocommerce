package com.bread.productservice.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GlobalExceptionHandler implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable ex, DataFetchingEnvironment environment) {
        if (ex instanceof MethodArgumentNotValidException manve) {
            String errorMessage = manve.getBindingResult().getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            GraphQLError error = GraphqlErrorBuilder.newError(environment)
                    .message("Validation failed: " + errorMessage)
                    .build();

            return Mono.just(List.of(error));
        } else if (ex instanceof ConstraintViolationException cve) {
            String errorMessage = cve.getConstraintViolations()
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));

            GraphQLError error = GraphqlErrorBuilder.newError(environment)
                    .message("Validation failed: " + errorMessage)
                    .build();

            return Mono.just(List.of(error));
        }

        return Mono.empty();
    }
}
