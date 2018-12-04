package com.example.android.filmmein;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickHandler {
    /*********************************************
     * The main class that displays a Recycler   *
     * full of Movie Posters to be clicked on    *
     *********************************************/


    //TEST VARIABLES
    private final String TEST_URL_STRING = "https://api.themoviedb.org/3/search/movie?api_key={api-key}&query=Fight";

    private final String TEST_JSON_STRING = "{\"page\":1,\"total_results\":1264,\"total_pages\":64,\"results\":[{\"vote_count\":14394,\"id\":550,\"video\":false,\"vote_average\":8.4,\"title\":\"Fight Club\",\"popularity\":27.116,\"poster_path\":\"\\/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg\",\"original_language\":\"en\",\"original_title\":\"Fight Club\",\"genre_ids\":[18],\"backdrop_path\":\"\\/87hTDiay2N2qWyX4Ds7ybXi9h8I.jpg\",\"adult\":false,\"overview\":\"A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \\\"fight clubs\\\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.\",\"release_date\":\"1999-10-15\"},{\"vote_count\":611,\"id\":345922,\"video\":false,\"vote_average\":6,\"title\":\"Fist Fight\",\"popularity\":7.701,\"poster_path\":\"\\/yONLyCSO0zyDvmVJO2i1U4yrNHE.jpg\",\"original_language\":\"en\",\"original_title\":\"Fist Fight\",\"genre_ids\":[35],\"backdrop_path\":\"\\/yzAo91sIwQrbYSh9bQrbQ12wZnI.jpg\",\"adult\":false,\"overview\":\"When one school teacher gets the other fired, he is challenged to an after-school fight.\",\"release_date\":\"2017-02-16\"},{\"vote_count\":0,\"id\":560441,\"video\":false,\"vote_average\":0,\"title\":\"They Fight\",\"popularity\":2.577,\"poster_path\":\"\\/syNlYrveHEcg1Ib04xId7baIVka.jpg\",\"original_language\":\"en\",\"original_title\":\"They Fight\",\"genre_ids\":[99],\"backdrop_path\":\"\\/9FY9IdZkWvxvfx5iQROL0nFTT4Q.jpg\",\"adult\":false,\"overview\":\"Coach Walt Manigan mentors young boxers at his after-school program in Washington, D.C.'s Ward 8.\",\"release_date\":\"2018-11-11\"},{\"vote_count\":28,\"id\":325365,\"video\":false,\"vote_average\":6.6,\"title\":\"Dawg Fight\",\"popularity\":2.365,\"poster_path\":\"\\/faxT9iweirNGU8oDQTdrTkwfacf.jpg\",\"original_language\":\"en\",\"original_title\":\"Dawg Fight\",\"genre_ids\":[99],\"backdrop_path\":\"\\/qStqSukfgt2LWN5Y3xb4STYVN35.jpg\",\"adult\":false,\"overview\":\"In a crime-plagued neighborhood near Miami, brutal, bare-knuckled backyard fights give young men a chance to earn money -- and self-respect.\",\"release_date\":\"2015-03-13\"},{\"vote_count\":0,\"id\":482986,\"video\":false,\"vote_average\":0,\"title\":\"The Fight\",\"popularity\":2.317,\"poster_path\":\"\\/uXrvRFcy7YJSyhtPpeaxKdXYHd3.jpg\",\"original_language\":\"en\",\"original_title\":\"The Fight\",\"genre_ids\":[35,18],\"backdrop_path\":null,\"adult\":false,\"overview\":\"Tina lives in a quiet seaside town but her life is anything but quiet - her mother is threatening to leave her father, her daughter is being bullied and she and her husband Mick are juggling full time jobs and three children. Determined to ditch the dysfunction and beat her inner demons, Tina puts on her fighting gloves - literally, stepping into the boxing ring to sweat out her anxieties and punch up her self-worth. But does she have what it takes to get her family off the ropes and emerge victorious?\",\"release_date\":\"2018-10-17\"},{\"vote_count\":0,\"id\":158301,\"video\":false,\"vote_average\":0,\"title\":\"Fight\",\"popularity\":0.6,\"poster_path\":null,\"original_language\":\"en\",\"original_title\":\"Kavgamız\",\"genre_ids\":[],\"backdrop_path\":null,\"adult\":false,\"overview\":\"A love story between a journalist and the daughter of a mafia godfather.\",\"release_date\":\"1989-01-01\"},{\"vote_count\":0,\"id\":456933,\"video\":false,\"vote_average\":0,\"title\":\"After School Knife Fight\",\"popularity\":2.844,\"poster_path\":\"\\/sw4Xxzdn0TmJxhEnTpwhIZDpwyh.jpg\",\"original_language\":\"en\",\"original_title\":\"After School Knife Fight\",\"genre_ids\":[],\"backdrop_path\":null,\"adult\":false,\"overview\":\"Laëtitia, Roca, Nico and Naël are in a band. As per usual, they meet late afternoon before rehearsal in a vacant lot. The group will soon no longer exist; they will be forced to disband because Laetitia is moving away to study. This is the story of four young adults who do not want to say good-bye.\",\"release_date\":\"2017-05-21\"},{\"vote_count\":13,\"id\":385383,\"video\":false,\"vote_average\":3.7,\"title\":\"Fight Valley\",\"popularity\":1.744,\"poster_path\":\"\\/jIPWkzF9srlU8eZTldLM6JYZwkO.jpg\",\"original_language\":\"en\",\"original_title\":\"Fight Valley\",\"genre_ids\":[28,18],\"backdrop_path\":\"\\/orRyeLPHjCtUqHjW1HJ3PnY5yzb.jpg\",\"adult\":false,\"overview\":\"When Tory Coro turns up dead, the neighborhood turns up silent. Rumor has it she became yet another victim of the small town known as FIGHT VALLEY. Tory's sister Windsor moves to town to begin her own investigation on her sister's mysterious death after weeks of no leads from the police. She's quick to learn that Tory fought for money to make ends meet. If girl-next-door Windsor is going to make her way into FIGHT VALLEY to find the truth about Tory, she's going to have to fight her way in. \\\"Jabs\\\" (Miesha Tate) swore she would never throw a punch in the Valley again. Jabs now finds herself training Windsor to survive the painful, unexpected path she's about to take. Every corner. Every alley. Every doorway. She must follow the last footsteps of her sister in order to come face-to-face with Tory's killer in FIGHT VALLEY.\",\"release_date\":\"2016-07-22\"},{\"vote_count\":44,\"id\":14286,\"video\":false,\"vote_average\":7.7,\"title\":\"Why We Fight\",\"popularity\":2.704,\"poster_path\":\"\\/kfOmnlwt1rrhxmxc05X3i9mHSOs.jpg\",\"original_language\":\"en\",\"original_title\":\"Why We Fight\",\"genre_ids\":[99,18,36],\"backdrop_path\":\"\\/rDwvllkfMDNs7CFb8AXwAVjNfnO.jpg\",\"adult\":false,\"overview\":\"Is American foreign policy dominated by the idea of military supremacy? Has the military become too important in American life? Jarecki's shrewd and intelligent polemic would seem to give an affirmative answer to each of these questions.\",\"release_date\":\"2005-01-01\"},{\"vote_count\":9,\"id\":490928,\"video\":false,\"vote_average\":6.6,\"title\":\"Craig Ferguson: Tickle Fight\",\"popularity\":2.651,\"poster_path\":\"\\/hR7q7l5gCGjVTGOiV7bYe6RNj0m.jpg\",\"original_language\":\"en\",\"original_title\":\"Craig Ferguson: Tickle Fight\",\"genre_ids\":[35],\"backdrop_path\":\"\\/ghBKDYFcDOOFu63AiJgh55xCbRx.jpg\",\"adult\":false,\"overview\":\"Cheeky comic Craig Ferguson keeps it casual as he discusses '70s porn, Japanese toilets and his mildly crime-filled days as a talk show host.\",\"release_date\":\"2017-12-05\"},{\"vote_count\":19,\"id\":39342,\"video\":false,\"vote_average\":6.1,\"title\":\"Fight for Your Life\",\"popularity\":2.626,\"poster_path\":\"\\/5LKQkKgkgip8PB8Is4doO94GI2L.jpg\",\"original_language\":\"en\",\"original_title\":\"Fight for Your Life\",\"genre_ids\":[53,80,18],\"backdrop_path\":\"\\/bBDyMixUi3TcWFtcR8fcoAhFqM6.jpg\",\"adult\":false,\"overview\":\"A minister dispenses justice on three convicts who take his family hostage.\",\"release_date\":\"1977-10-03\"},{\"vote_count\":2,\"id\":559578,\"video\":false,\"vote_average\":8,\"title\":\"Alone We Fight\",\"popularity\":2.54,\"poster_path\":\"\\/y0QXD8zSxpBsyQSKN9mg5diYexV.jpg\",\"original_language\":\"en\",\"original_title\":\"Alone We Fight\",\"genre_ids\":[10752],\"backdrop_path\":\"\\/iOb04yKGv80N8BNWYLuvBr4wLv7.jpg\",\"adult\":false,\"overview\":\"Facing mounting odds, a small but determined band of American soldiers venture into dangerous enemy territory on a mission to stop an advancing German unit from breaking through the Allied line.\",\"release_date\":\"2018-11-06\"},{\"vote_count\":0,\"id\":551729,\"video\":false,\"vote_average\":0,\"title\":\"Free Fight\",\"popularity\":1.516,\"poster_path\":\"\\/6JOlTOD6tuJVs7w5ZJU4tJKbDNZ.jpg\",\"original_language\":\"en\",\"original_title\":\"Free Fight\",\"genre_ids\":[18],\"backdrop_path\":\"\\/9w30TSgEKSEKJWPMnEEX1gG7yYS.jpg\",\"adult\":false,\"overview\":\"They do not use many words, but brothers Joes and Matthias master the body language all the better. They train continuously for the MMA championship. During these sessions, their bodies are in complete harmony. However, shortly before an important fight, their mother passes away. Suddenly, the brothers need more than their biceps and their fighting instinct.\",\"release_date\":\"2018-11-03\"},{\"vote_count\":16,\"id\":108251,\"video\":false,\"vote_average\":6.1,\"title\":\"Girl Fight\",\"popularity\":1.446,\"poster_path\":\"\\/mie2uVWWI2iNlkSdHaOjk1J3irW.jpg\",\"original_language\":\"en\",\"original_title\":\"Girl Fight\",\"genre_ids\":[18,10770],\"backdrop_path\":\"\\/vDvDF9MKOyg42AmWj4EXw3wp0Je.jpg\",\"adult\":false,\"overview\":\"Inspired by a true story, Girl Fight recounts the harrowing story of a 16-year-old, stellar high school student whose life spirals downward when her former friends conspire to upload onto the Internet a shocking video of them beating her up.\",\"release_date\":\"2011-10-03\"},{\"vote_count\":16,\"id\":440777,\"video\":false,\"vote_average\":5.3,\"title\":\"Female Fight Club\",\"popularity\":2.176,\"poster_path\":\"\\/o6Sa24tGF9va8FnMtR5Uj7Icx8H.jpg\",\"original_language\":\"en\",\"original_title\":\"Female Fight Club\",\"genre_ids\":[18,28],\"backdrop_path\":\"\\/rYyIqfx6BNpb7s0Wy5OqddCsKEQ.jpg\",\"adult\":false,\"overview\":\"A former fighter reluctantly returns to the life she abandoned in order to help her sister survive the sadistic world of illegal fighting and the maniac who runs it.\",\"release_date\":\"2017-03-16\"},{\"vote_count\":9,\"id\":14216,\"video\":false,\"vote_average\":4.7,\"title\":\"Rigged\",\"popularity\":1.759,\"poster_path\":\"\\/5UJgReICd8TSvA2DcejXuHcx38P.jpg\",\"original_language\":\"en\",\"original_title\":\"Rigged\",\"genre_ids\":[18,28,53,80],\"backdrop_path\":\"\\/qH1vhEGii7FQjc5Ywbn8YSnAXiZ.jpg\",\"adult\":false,\"overview\":\"In the seedy underground of illegal prizefighting, a corrupt boxing promoter is embroiled in a dangerous fight-fixing scheme with his female prizefighter.\",\"release_date\":\"2008-09-10\"},{\"vote_count\":41,\"id\":45966,\"video\":false,\"vote_average\":5.5,\"title\":\"Day of the Fight\",\"popularity\":1.835,\"poster_path\":\"\\/4caX3IZkf7n2QCbxaJO3hyn0SCm.jpg\",\"original_language\":\"en\",\"original_title\":\"Day of the Fight\",\"genre_ids\":[99],\"backdrop_path\":\"\\/cjqyykBlWldT1CWjkeFcyj4r9Jf.jpg\",\"adult\":false,\"overview\":\"American short subject documentary film in black-and-white, which is notable as the first picture directed by Stanley Kubrick. Kubrick financed the film himself, and it is based on an earlier photo feature he had done as a photographer for Look magazine in 1949. 'Day of the Fight' shows Irish-American middleweight boxer Walter Cartier during the height of his career, on the day of a fight with black middleweight Bobby James, which took place on April 17, 1950.\",\"release_date\":\"1951-04-26\"},{\"vote_count\":16,\"id\":289732,\"video\":false,\"vote_average\":3.2,\"title\":\"Zombie Fight Club\",\"popularity\":1.785,\"poster_path\":\"\\/7k9db7pJyTaVbz3G4eshGltivR1.jpg\",\"original_language\":\"zh\",\"original_title\":\"Zombie Fight Club\",\"genre_ids\":[28,27],\"backdrop_path\":\"\\/qrZssI8koUdRxkYnrOKMRY3m5Fq.jpg\",\"adult\":false,\"overview\":\"It's the end of the century at a corner of the city in a building riddled with crime - Everyone in the building has turned into zombies. After Jenny's boyfriend is killed in a zombie attack, she faces the challenge of surviving in the face of adversity. In order to stay alive, she struggles with Andy to flee danger.\",\"release_date\":\"2014-10-23\"},{\"vote_count\":1,\"id\":169583,\"video\":false,\"vote_average\":4,\"title\":\"Gun Fight\",\"popularity\":1.163,\"poster_path\":\"\\/l0kPTgldcYYqHbP7XFCRyGKrC9e.jpg\",\"original_language\":\"en\",\"original_title\":\"Gun Fight\",\"genre_ids\":[37],\"backdrop_path\":\"\\/9K7O1J91X8hqraCbvMDAha5ahHp.jpg\",\"adult\":false,\"overview\":\"Action Western directed by Edward L. Cahn . After courageously protecting a pretty dance hall girl (Joan Staley) from peril, former cavalry soldier Wayne (James Brown) refuses to join his brother Brad's (Gregg Palmer) unlawful cattle rustling gang, which leads to heated disagreements, bitter betrayals and life-threatening danger.\",\"release_date\":\"1961-04-30\"},{\"vote_count\":90,\"id\":355020,\"video\":false,\"vote_average\":8.1,\"title\":\"Winter on Fire: Ukraine's Fight for Freedom\",\"popularity\":4.521,\"poster_path\":\"\\/tIfw9XIYnP0oygZgwPScK421yHW.jpg\",\"original_language\":\"uk\",\"original_title\":\"Winter on Fire: Ukraine's Fight for Freedom\",\"genre_ids\":[99],\"backdrop_path\":\"\\/wWX8L8ujV8AL8dFZ8h9quqcewE.jpg\",\"adult\":false,\"overview\":\"A documentary on the unrest in Ukraine during 2013 and 2014, as student demonstrations supporting European integration grew into a violent revolution calling for the resignation of President Viktor F. Yanukovich.\",\"release_date\":\"2015-10-09\"}]}";

    //END OF TEST VARIABLES

    //Member Variables
    private RecyclerView mResultsRecycler;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign member variables
        mResultsRecycler = (RecyclerView) findViewById(R.id.movie_rv);

        //Measure screen size and determine number of columns that will fit
        int numberOfColumns = Utilities.getNumberOfColumns(this);

        //Logic to Setup RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns, LinearLayoutManager.VERTICAL, false);
        mResultsRecycler.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mResultsRecycler.setAdapter(mMovieAdapter);

        //TODO Create custom ItemDecoration to space grid items better

        //Temporary set of movie data for testing
        List<Movie> movies = Utilities.parseJSON(this, TEST_JSON_STRING);

        mMovieAdapter.setNewsStories(movies);


    }

    //Menu creation logic
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.action_about) :
                Intent aboutIntent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(aboutIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Item Click Logic, use the movie that was passed from the ViewHolder's onClickListener, and start intent
    @Override
    public void onItemClick(Movie movie) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(getString(R.string.intent_extra_key), movie);
        startActivity(detailIntent);
    }
}
