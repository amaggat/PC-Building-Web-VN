package backend.chatbot.service;

import backend.component.cpu.repo.CpuRepository;
import backend.component.drives.HardDiskDrive.HddRepository;
import backend.component.drives.SolidStateDrive.SsdRepository;
import backend.component.gpu.GpuRepository;
import backend.component.mainboard.MainRepository;
import backend.component.psu.PsuRepository;
import backend.component.ram.RamRepository;
import backend.utility.Recommender;
import backend.utility.Result;
import backend.utility.Utility;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatbotServiceImpl implements ChatbotService{

    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final MainRepository mainRepository;
    private final RamRepository ramRepository;
    private final SsdRepository ssdRepository;
    private final HddRepository hddRepository;
    private final PsuRepository psuRepository;


    public ChatbotServiceImpl(CpuRepository cpuRepository, GpuRepository gpuRepository, MainRepository mainRepository, RamRepository ramRepository, SsdRepository ssdRepository, HddRepository hddRepository, PsuRepository psuRepository) {
        this.cpuRepository = cpuRepository;
        this.gpuRepository = gpuRepository;
        this.mainRepository = mainRepository;
        this.ramRepository = ramRepository;
        this.ssdRepository = ssdRepository;
        this.hddRepository = hddRepository;
        this.psuRepository = psuRepository;
    }

    public List returnRecommendList(Integer id, String type, String url) {
        List recommendList = new ArrayList();
        try {
            Result result = Utility.returnReccomendedItem(null, type, id);
            recommendList = getComponentId(result, type, url);
        } catch (Exception e) {
            return recommendList;
        }

        return recommendList;
    }

   private List getComponentId(Result result, String type, String url) {
        List componentId = new ArrayList();
        Collections.shuffle(result.getResult());
        for (int i = 0; i <2; ++i) {
            Recommender recommender = result.getResult().get(i);
            String link = "http://localhost:3000/products/" + url + "/" + recommender.getItem();
            Pair<String, String> product = new Pair<>(getComponentName(recommender, type), link);
            componentId.add(product);
        }

        return componentId;
   }

   private String getComponentName(Recommender recommender, String type) {
        String componentName = new String();
        switch (type){
            case "cpu":
                componentName = cpuRepository.findByID(recommender.getItem()).getFullname();
                break;
            case "gpu":
                componentName = gpuRepository.findByID(recommender.getItem()).getFullname();
                break;
            case "mainboard":
                componentName = mainRepository.findByID(recommender.getItem()).getFullname();
                break;
            case "ram":
                componentName = ramRepository.findByID(recommender.getItem()).getFullname();
                break;
            case "ssd":
                componentName = ssdRepository.findByID(recommender.getItem()).getFullname();
                break;
            case "hdd":
                componentName = hddRepository.findByID(recommender.getItem()).getFullname();
                break;
            case "psu":
                componentName = psuRepository.findByID(recommender.getItem()).getFullname();
                break;
        }
        return componentName;
   }

}
