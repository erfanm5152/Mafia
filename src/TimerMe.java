public class TimerMe implements Runnable{

    private int time;
    private boolean end;

    public TimerMe(int time) {
        this.time = time;
        this.end = false;
    }

    @Override
    public void run() {
        while (time != 0) {
            System.out.println("    "+time);
            time--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        end = true;
        System.out.println(123456);
    }

    public boolean isEnd() {
        System.out.println(78910);
        return end;
    }
}