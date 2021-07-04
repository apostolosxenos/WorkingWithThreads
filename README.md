# Working with Java Threads

This project deals with Java Threads and Concurrency. It clearly shows the advantage of using multiple threads in parallel to perform specific calculations. 
A comparison of execution times for different number of threads (1, 2, 4, 8) is provided in the console for each task.

# Task 1

The program fills an array with 2^20 randomly generated passwords. These passwords consist of at least 8 to maximum 16 small and capital letters combined. Each thread runs on a specific part of the array and sums the occurence of each character found in a synchronized way to avoid interference.

# Task 2

The program collects data from a .csv file regarding COVID-19 cases and deaths worldwide. The .csv file is available at ECDC (https://www.ecdc.europa.eu/en/publications-data/download-todays-data-geographic-distribution-covid-19-cases-worldwide). After file parsing all data is loaded in the memory and i use an increasing number of threads to calculate:
a) Total cases for each country
b) Total cases per date
c) The country that for a specific date had the most cases proportionally to its population.
and export them to txt files.

# Task 3

In this task i make HTTP calls on an open API using multiple threads to export statistics regarding
a) Minimum and maximum number of characters from all API calls
b) The frequency of occurence for each character

# Task 4

In the last task, i demonstrate the Producer-Consumer problem using a thread (COVID) that constantly produces cases and three threads (Hospitals) that constantly heal cases. I implemented a LinkedBlockingQueue collection to solve this problem.
