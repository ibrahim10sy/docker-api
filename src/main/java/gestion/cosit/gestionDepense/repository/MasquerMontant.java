package gestion.cosit.gestionDepense.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gestion.cosit.gestionDepense.model.MontantSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = MontantSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public @interface MasquerMontant {
}
