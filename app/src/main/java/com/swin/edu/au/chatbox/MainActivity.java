package com.swin.edu.au.chatbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swin.edu.au.chatbox.FragmentModel.ChatFragment;
import com.swin.edu.au.chatbox.FragmentModel.UserFragment;
import com.swin.edu.au.chatbox.Model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView pf_image;
    TextView userName;
    FirebaseUser firebaseUser;
    DatabaseReference ref;
    Toolbar tbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tbar= findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle("");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pf_image = findViewById(R.id.pf_image);
        userName = findViewById(R.id.userName);


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user= dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());

                if(user.getImgURL().equals("default"))
                {
                    pf_image.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(MainActivity.this).load(user.getImgURL()).into(pf_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatFragment(),"Chats");
        viewPagerAdapter.addFragment(new UserFragment(),"Users");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,MainActivity2.class));
                finish();
                return  true;

            case R.id.userPf:
                startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
                finish();
                return  true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> title;


        ViewPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
            this.fragments=new ArrayList<>();
            this.title= new ArrayList<>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public  void addFragment(Fragment fragment, String titleTxt)
        {
            fragments.add(fragment);
            title.add(titleTxt);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }
}
