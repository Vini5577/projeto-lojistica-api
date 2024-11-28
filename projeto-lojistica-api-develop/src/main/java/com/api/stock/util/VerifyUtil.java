package com.api.stock.util;

import org.springframework.stereotype.Service;

@Service
public class VerifyUtil {

    public String validateCnpj(String cnpj) {

        cnpj = cleanNumber(cnpj);

        if(cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d+")) {
            return null;
        }

        cnpj = formatCnpj(cnpj);

        return cnpj;
    }

    public String formatCnpj(String cnpj) {
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    private String cleanNumber(String number) {
        if (number != null) {
            return number.replaceAll("\\D", "");
        }

        return null;
    }

    public String validateTelefone(String telefone) {

        telefone = cleanNumber(telefone);

        if (telefone == null || (telefone.length() != 11 && telefone.length() != 10) || !telefone.matches("\\d+")) {
            return null;
        }

        telefone = formatTelefone(telefone);

        return telefone;
    }

    private String formatTelefone(String telefone) {
        return telefone.length() == 11
                ? telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3")
                : telefone.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }
}
