# ICSODE: Improved Chaotic Social Optimization with Differential Evolution

This repository contains the source code and dependencies for the research paper:

**"ICSODE: An Improved Chaotic Social Optimization Algorithm with Differential Evolution for Job Scheduling in Cloud Environments"**  
📌 [Published in Springer - The Journal of Supercomputing (2025)](https://doi.org/10.1007/s11227-025-07206-w)

---

## 📖 Abstract
Cloud computing dynamically allocates computational resources to meet user
demands, but task scheduling remains an NP-hard optimization problem due to
multiple constraints such as task dependencies, execution time, and resource allocation. Metaheuristic algorithms have been widely used to tackle these challenges,yet existing approaches often suffer from premature convergence, poor exploration–exploitation balance, and inefficiencies in high-dimensional search spaces.To overcome these limitations, this study proposes an Improved Chicken Swarm
Optimization with Differential Evolution (ICSODE), which integrates chaos-based adaptive parameter tuning to dynamically adjust the learning coefficient C for better convergence and Differential Evolution (DE)–based mutation and recombination to enhance population diversity and global search efficiency. A Dynamic Scheduling Framework (DSF.ICSODE) is also introduced, leveraging ICSODE to efficiently allocate independent tasks to virtual machines (VMs) in a cloud computing environment. The proposed ICSODE algorithm is first evaluated on CEC2017 benchmark functions, demonstrating superior mean error reduction compared to CSO, ICSO,ICSOCrossover, ICSOChaotic, and DE. In the second phase, ICSODE is applied within the DSF.ICSODE scheduling framework and tested on the NASA-iPSC parallel workload dataset, where it significantly reduces execution time by an average of 15.36% and improves response time by an average of 38.65%. Experimental results confirm that ICSODE outperforms existing CSO variants and DE in both function optimization and dynamic task scheduling, establishing it as a highly effective solution for cloud-based scheduling challenges. 

---

## 📁 Repository Structure

```
ICSODE_project/
│
├── cloudsim-3.0.3/           # CloudSim simulation engine (Java)
├── commons-math3-3.2/        # Apache Commons Math library for numerical computations
├── cloudsim_guide/           # User guide and sample configurations
├── Dataset2017.docx          # Input dataset specifications
├── Document_Project_Part.pdf # Internal project documentation
├── LCG-2005-0                # Legacy scheduling implementation (optional)
```

---

## 🚀 How to Run

1. Install [Java JDK 8+](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html).
2. Open the project in an IDE like **Eclipse** or **NetBeans**.
3. Add `cloudsim-3.0.3` and `commons-math3-3.2` to the project's classpath.
4. Compile and run the main scheduling class, usually located in:
   ```
   cloudsim-3.0.3/src/org/cloudbus/cloudsim/examples/...
   ```

---

## 📊 Results Summary

ICSODE was tested on synthetic workloads using CloudSim. Compared to classical and recent algorithms (PSO, ACO, GA, etc.), ICSODE achieved:
- Lower average makespan
- Higher resource utilization
- Faster convergence rate

See full results in the [📄 published article](https://doi.org/10.1007/s11227-025-07206-w).

---

## 📌 Citation

If you use this code or refer to our algorithm in your research, please cite:

```bibtex
@article{safi2025dynamic,
  author    = {Safi-Esfahani, Faramarz and Larian, Hamed and Saeedi Mobarakeh, Seyed and others},
  title     = {Dynamic scheduling of independent tasks in cloud computing environment applying improved chicken swarm optimization and differential evolution},
  journal   = {The Journal of Supercomputing},
  volume    = {81},
  pages     = {867},
  year      = {2025},
  publisher = {Springer},
  doi       = {10.1007/s11227-025-07206-w},
  url       = {https://doi.org/10.1007/s11227-025-07206-w}
}

```

---

## 🧑‍💻 Authors

- Faramarz Safi-Esfahani, 
- Habib Larian, 
- Saeed Saeedi Mobarakeh 
- Seyedali Mirjalili   
  
[GitHub Profile](https://github.com/faramarzsafi)

---

## 📬 Contact

For questions, collaborations, or issues, please reach out via GitHub or email.