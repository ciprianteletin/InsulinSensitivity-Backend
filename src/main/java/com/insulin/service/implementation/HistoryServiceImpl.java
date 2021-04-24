package com.insulin.service.implementation;

import com.insulin.exceptions.model.InvalidHistoryId;
import com.insulin.exceptions.model.UserNotFoundException;
import com.insulin.model.History;
import com.insulin.model.User;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.repository.HistoryRepository;
import com.insulin.repository.UserRepository;
import com.insulin.service.abstraction.HistoryService;
import com.insulin.utils.HistoryUtils;
import com.insulin.utils.model.IndexSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.insulin.shared.constants.ExceptionConstants.HISTORY_ID;
import static com.insulin.utils.HistoryUtils.*;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveHistory(User user, MandatoryInsulinInformation mandatoryInformation, String result, IndexSender indexSender) {
        History history = buildHistory(mandatoryInformation, result, indexSender);
        user.addHistory(history);
        historyRepository.save(history);
        userRepository.save(user);
    }

    @Override
    public List<IndexSummary> findSummaryByUsername(String username) throws UserNotFoundException {
        User user = getUserByUsernameOrThrowError(username);
        List<History> relatedHistory = user.getHistoryList();
        return relatedHistory.stream().map(HistoryUtils::convertHistoryToSummary)
                .collect(Collectors.toList());
    }

    // TODO maybe check if the logged user has an history object with that id.
    @Override
    public Pair<MandatoryInsulinInformation, IndexSender> getMandatorySenderPairByHistoryId(Long id, String username)
            throws InvalidHistoryId, UserNotFoundException {
        History history = historyRepository.findById(id)
                .orElseThrow(() -> new InvalidHistoryId(HISTORY_ID));
        User user = getUserByUsernameOrThrowError(username);
        MandatoryInsulinInformation mandatoryInformation = convertHistoryToMandatory(history);
        addUserData(user, mandatoryInformation);
        IndexSender sender = convertHistoryToSender(history);
        return Pair.of(mandatoryInformation, sender);
    }

    @Override
    public void deleteByHistoryId(Long historyId) {
        historyRepository.deleteById(historyId);
    }

    private User getUserByUsernameOrThrowError(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    private void addUserData(User user, MandatoryInsulinInformation mandatoryInformation) {
        mandatoryInformation.setFullName(user.getDetails().getFirstName() + " " + user.getDetails().getLastName());
        String gender;
        if (user.getDetails().getGender() == 'M') {
            gender = "Male";
        } else {
            gender = "Female";
        }
        mandatoryInformation.setGender(gender);
        int age = convertBirthDayToAge(user.getDetails().getBirthDay());
        mandatoryInformation.setAge(age);
    }

    private int convertBirthDayToAge(String birthDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(birthDay, formatter);

        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
