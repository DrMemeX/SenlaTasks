package cvs.applier;

public abstract class AbstractCsvDtoApplier<D, E> {

    protected void checkField(String field, RuntimeException exception) {
        if (field == null || field.isBlank()) {
            throw exception;
        }
    }

    public abstract E apply(D dto);
}
