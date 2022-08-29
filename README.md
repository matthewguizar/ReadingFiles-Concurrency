# ReadingFiles-Concurrency

Putting together Executor service to run a fixed number of threads and start them without a lot of coding. Then using Countdown latch
to allow threads to go one after another, but leads to Race Conditions so added ReentrantLock to make sure each threads waits 
until the one before is finished.

what is happening is the increment method reads files using Path to read and parse through data(sales) to calculate
the sample size and quantity that was sold. since there are lots of data using threads is needed.
