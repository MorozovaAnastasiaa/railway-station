package com.railway.RailwayStation3.repository;

import com.railway.RailwayStation3.model.Train;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с поездами.
 * Предоставляет доступ к данным о поездах через Spring Data JPA.
 */
public interface TrainRepository extends JpaRepository<Train, Long> {

    /**
     * Находит поезд по его номеру.
     *
     * @param number номер поезда
     * @return Optional с найденным поездом или пустой объект, если поезд не найден
     */
    Optional<Train> findByNumber(String number);

    /**
     * Находит список поездов по городам отправления и прибытия, а также дате отправления.
     * Может применять сортировку.
     *
     * @param fromCity город отправления
     * @param toCity город прибытия
     * @param departureDate дата отправления
     * @param sort способ сортировки результатов
     * @return список подходящих поездов
     */
    List<Train> findByFromCityAndToCityAndDepartureDate(
            String fromCity,
            String toCity,
            LocalDate departureDate,
            Sort sort);

    /**
     * Возвращает популярные направления по количеству поездов (в порядке убывания).
     * Используется в разделе статистики.
     *
     * @return список массивов объектов: [направление, количество]
     */
    @Query("SELECT CONCAT(t.fromCity, ' → ', t.toCity) as direction, COUNT(t) as count " +
            "FROM Train t GROUP BY direction ORDER BY count DESC")
    List<Object[]> findPopularDirections();

    /**
     * Возвращает уникальные города отправления для выпадающего списка фильтрации.
     *
     * @return отсортированный список городов отправления
     */
    @Query("SELECT DISTINCT t.fromCity FROM Train t ORDER BY t.fromCity")
    List<String> findDistinctFromCities();

    /**
     * Возвращает уникальные города прибытия для выпадающего списка фильтрации.
     *
     * @return отсортированный список городов прибытия
     */
    @Query("SELECT DISTINCT t.toCity FROM Train t ORDER BY t.toCity")
    List<String> findDistinctToCities();

    /**
     * Проверяет, существует ли поезд с указанным номером.
     *
     * @param number номер поезда
     * @return true, если такой поезд уже существует
     */
    boolean existsByNumber(String number);
}